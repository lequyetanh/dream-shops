package com.dailycodework.dreamshops.service.order;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.order.OrderInfo;
import com.dailycodework.dreamshops.payload.dto.orderProduct.OrderProductReq;
import com.dailycodework.dreamshops.entity.Order;
import com.dailycodework.dreamshops.entity.OrderProduct;
import com.dailycodework.dreamshops.entity.TaskLog;
import com.dailycodework.dreamshops.payload.dto.taskLog.Content;
import com.dailycodework.dreamshops.rabbitmq.producer.OrderProducer;
import com.dailycodework.dreamshops.repository.order.IOrderRepository;
import com.dailycodework.dreamshops.repository.product.IProductRepository;
import com.dailycodework.dreamshops.service.taskLog.TaskLogService;
import com.dailycodework.dreamshops.util.Common;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final OrderProducer orderProducer;
    private final TaskLogService taskLogService;
    private final OrderWebSocketService orderWebSocketService;

    @Override
    @Transactional(readOnly = true)
    public BaseResultDTO getOrderWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            String orderCode,
            Integer status,
            Integer companyId
    ){
        Page<OrderInfo> orderList = orderRepository.getOrderWithPaging(
                pageable, keyword, fromDate, toDate, orderCode, status, companyId
        );
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                orderList.getContent(),
                (int) orderList.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResultDTO findById(Long id){
        Optional<Order> order = orderRepository.findById(id);
        return order.map(value -> new BaseResultDTO(ResultNotify.successGet, true, value))
                .orElseGet(() -> new BaseResultDTO(ResultNotify.notFound, false, null));
    }

    @Override
    public BaseResultDTO createOrder(OrderInfo orderReq){
        Order order = new Order();
        List<OrderProduct> productList = new ArrayList<>();
        for (OrderProductReq prod : orderReq.getProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            BeanUtils.copyProperties(prod, orderProduct);
            orderProduct.setOrder(order);
            productList.add(orderProduct);
        }
        BeanUtils.copyProperties(orderReq, order);
        order.setProducts(productList);
        order.setOrderDate(orderReq.getOrderDate());
        orderRepository.save(order);

        deductStock(orderReq.getProducts());

        TaskLog taskLog = new TaskLog();
        Content content = new Content();
        content.setBillIds(List.of(order.getId()));
        taskLog.setContent(Common.toJsonString(content));
        taskLogService.createTaskLog(taskLog);
        orderProducer.createOrderQueue(taskLog.getId());

        orderWebSocketService.notifyOrderCreated(order);

        return new BaseResultDTO(ResultNotify.successCreate, true, order);
    }

    @Override
    public BaseResultDTO updateOrder(OrderInfo orderReq){
        Optional<Order> existingOrderOpt = orderRepository.findById(orderReq.getId());
        if (existingOrderOpt.isEmpty()) {
            return new BaseResultDTO(ResultNotify.notFound, false, null);
        }
        Order order = existingOrderOpt.get();

        restoreStock(order.getProducts());
        order.getProducts().clear();

        List<OrderProduct> productList = new ArrayList<>();
        for (OrderProductReq prod : orderReq.getProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            BeanUtils.copyProperties(prod, orderProduct);
            orderProduct.setOrder(order);
            productList.add(orderProduct);
        }

        order.setCode(orderReq.getCode());
        order.setCustomerId(orderReq.getCustomerId());
        order.setDescription(orderReq.getDescription());
        order.setDiscountAmount(orderReq.getDiscountAmount());
        order.setVatRate(orderReq.getVatRate());
        order.setVatAmount(orderReq.getVatAmount());
        order.setTotalAmount(orderReq.getTotalAmount());
        order.setStatus(orderReq.getStatus());
        order.setExtra(orderReq.getExtra());
        order.setOrderDate(orderReq.getOrderDate());
        order.getProducts().addAll(productList);
        orderRepository.save(order);

        deductStock(orderReq.getProducts());

        orderWebSocketService.notifyOrderUpdated(order);

        return new BaseResultDTO(ResultNotify.successUpdate, true, order);
    }

    @Override
    public BaseResultDTO updateOrderStatus(Long id, Integer status){
        Optional<Order> existingOrderOpt = orderRepository.findById(id);
        if (existingOrderOpt.isEmpty()) {
            return new BaseResultDTO(ResultNotify.notFound, false, null);
        }
        Order order = existingOrderOpt.get();
        order.setStatus(status);
        orderRepository.save(order);

        orderWebSocketService.notifyOrderStatusChanged(order);

        return new BaseResultDTO(ResultNotify.successUpdate, true, order);
    }

    @Override
    public BaseResultDTO deleteOrder(Long id){
        Optional<Order> existingOrderOpt = orderRepository.findById(id);
        existingOrderOpt.ifPresent(order -> {
            restoreStock(order.getProducts());
            orderWebSocketService.notifyOrderDeleted(id, order.getCompanyId());
        });
        orderRepository.deleteById(id);
        return new BaseResultDTO(ResultNotify.successDelete, true, null);
    }

    private void deductStock(List<OrderProductReq> products) {
        for (OrderProductReq prod : products) {
            if (prod.getProductId() == null || prod.getQuantity() == null) continue;
            Optional<com.dailycodework.dreamshops.entity.Product> productOpt = productRepository.findById(prod.getProductId());
            if (productOpt.isPresent()) {
                com.dailycodework.dreamshops.entity.Product product = productOpt.get();
                if (product.getStockQuantity() != null) {
                    int newQty = product.getStockQuantity() - prod.getQuantity().intValue();
                    if (newQty < 0) {
                        throw new RuntimeException("Sản phẩm '" + product.getName() + "' không đủ số lượng trong kho (còn " + product.getStockQuantity() + ")");
                    }
                    product.setStockQuantity(newQty);
                    productRepository.save(product);
                }
            }
        }
    }

    private void restoreStock(List<OrderProduct> products) {
        for (OrderProduct prod : products) {
            if (prod.getProductId() == null || prod.getQuantity() == null) continue;
            productRepository.findById(prod.getProductId()).ifPresent(product -> {
                if (product.getStockQuantity() != null) {
                    product.setStockQuantity(product.getStockQuantity() + prod.getQuantity().intValue());
                    productRepository.save(product);
                }
            });
        }
    }
}
