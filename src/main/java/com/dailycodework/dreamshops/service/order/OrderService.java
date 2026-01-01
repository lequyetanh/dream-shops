package com.dailycodework.dreamshops.service.order;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.order.OrderInfo;
import com.dailycodework.dreamshops.repository.order.IOrderRepository;
import com.dailycodework.dreamshops.service.customer.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final IOrderRepository orderRepository;

    @Override
    public BaseResultDTO getOrderWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            String OrderCode,
            Integer Status
    ){
        return null;
    };

    @Override
    public BaseResultDTO findById(Long id){
        return null;
    };

    @Override
    public BaseResultDTO createOrder(OrderInfo orderReq){
        return null;
    };

    @Override
    public BaseResultDTO updateOrder(OrderInfo orderReq){
        return null;
    };

    @Override
    public BaseResultDTO deleteOrder(Long id){
        return null;
    };
}
