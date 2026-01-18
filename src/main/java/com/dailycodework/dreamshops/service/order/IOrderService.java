package com.dailycodework.dreamshops.service.order;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.order.OrderInfo;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    public BaseResultDTO getOrderWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            String OrderCode,
            Integer Status
    );
    public BaseResultDTO findById(Long id);
    public BaseResultDTO createOrder(OrderInfo orderReq);
    public BaseResultDTO updateOrder(OrderInfo orderReq);
    public BaseResultDTO deleteOrder(Long id);
}
