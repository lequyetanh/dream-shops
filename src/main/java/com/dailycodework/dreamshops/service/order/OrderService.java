package com.dailycodework.dreamshops.service.order;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.order.OrderInfo;
import com.dailycodework.dreamshops.dto.orderProduct.OrderProductReq;
import com.dailycodework.dreamshops.entity.Order;
import com.dailycodework.dreamshops.entity.OrderProduct;
import com.dailycodework.dreamshops.entity.Product;
import com.dailycodework.dreamshops.rabbitmq.producer.OrderProducer;
import com.dailycodework.dreamshops.repository.order.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;
    private final OrderProducer orderProducer;

    @Override
    public BaseResultDTO getOrderWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            String orderCode,
            Integer status
    ){
        List<OrderInfo> orderResponse = new ArrayList<>();
        Page<OrderInfo> orderList = orderRepository.getOrderWithPaging(
                pageable,
                keyword,
                fromDate,
                toDate,
                orderCode,
                status
        );
        orderResponse = orderList.getContent();
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                orderResponse
        );
    };

    @Override
    public BaseResultDTO findById(Long id){
        return null;
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
        orderRepository.save(order);
        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                order
        );

        // Gửi message vào queue để tạo đơn hàng
//        orderProducer.createOrderQueue(orderReq.getCompanyId());
//        return new BaseResultDTO(
//                ResultNotify.successCreate,
//                true,
//                null
//        );
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
