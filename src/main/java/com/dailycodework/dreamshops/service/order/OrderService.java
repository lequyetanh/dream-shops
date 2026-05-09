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
import com.dailycodework.dreamshops.service.taskLog.TaskLogService;
import com.dailycodework.dreamshops.util.Common;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;
    private final OrderProducer orderProducer;
    private final TaskLogService taskLogService;

    @Override
    public BaseResultDTO getOrderWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            String orderCode,
            Integer status,
            Integer companyId
    ){
        List<OrderInfo> orderResponse = new ArrayList<>();
        Page<OrderInfo> orderList = orderRepository.getOrderWithPaging(
                pageable,
                keyword,
                fromDate,
                toDate,
                orderCode,
                status,
                companyId
        );
        orderResponse = orderList.getContent();
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                orderResponse,
                (int) orderList.getTotalElements()
        );
    };

    @Override
    public BaseResultDTO findById(Long id){
        Optional<Order> order = orderRepository.findById(id);
        return order.map(value -> new BaseResultDTO(
                ResultNotify.successGet,
                true,
                value
        )).orElseGet(() -> new BaseResultDTO(
                ResultNotify.notFound,
                false,
                null
        ));
    };

    @Override
    public BaseResultDTO createOrder(OrderInfo orderReq){
        Order order = new Order();
        List<OrderProduct> productList = new ArrayList<>();
        for(OrderProductReq prod : orderReq.getOrderProductList()){
            OrderProduct orderProduct = new OrderProduct();
            BeanUtils.copyProperties(prod,orderProduct);
            orderProduct.setOrder(order);
            productList.add(orderProduct);
        }
        BeanUtils.copyProperties(orderReq,order);
        order.setProducts(productList);
        order.setOrderDate(orderReq.getOrderDate());
        orderRepository.save(order);

//        start sinh tasklog
        TaskLog taskLog = new TaskLog();
        Content content = new Content();
        content.setBillIds(List.of(order.getId()));
//        taskLog.setType("BILL_COMPLETION");
//        taskLog.setContent(Common.toJsonString("billIds:[" + orderReq.getId() + "]"));
        taskLog.setContent(Common.toJsonString(content));
        taskLogService.createTaskLog(taskLog);
//        end sinh tasklog
        orderProducer.createOrderQueue(taskLog.getId());

        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                order
        );
    };

    @Override
    public BaseResultDTO updateOrder(OrderInfo orderReq){
        return null;
    };

    @Override
    public BaseResultDTO deleteOrder(Long id){
        orderRepository.deleteById(id);
        return new BaseResultDTO(
                ResultNotify.successDelete,
                true,
                null
        );
    };
}
