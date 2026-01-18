package com.dailycodework.dreamshops.repository.order;

import com.dailycodework.dreamshops.dto.order.OrderInfo;
import org.springframework.data.domain.Page;
import java.awt.print.Pageable;

public interface OrderRepositoryCustom {
    Page<OrderInfo> getOrdersWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            String orderCode,
            Integer status
    );
}
