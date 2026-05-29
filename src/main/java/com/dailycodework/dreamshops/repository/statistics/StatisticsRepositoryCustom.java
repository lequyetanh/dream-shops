package com.dailycodework.dreamshops.repository.statistics;

import com.dailycodework.dreamshops.payload.dto.statistics.OrderStatusCountDTO;
import com.dailycodework.dreamshops.payload.dto.statistics.RevenueStatDTO;
import com.dailycodework.dreamshops.payload.dto.statistics.TopProductDTO;

import java.util.List;

public interface StatisticsRepositoryCustom {
    List<RevenueStatDTO> getRevenue(String fromDate, String toDate, String groupBy, Long companyId);
    List<TopProductDTO> getTopProducts(Long companyId, String fromDate, String toDate, int limit);
    List<OrderStatusCountDTO> getOrderStatusCount(Long companyId, String fromDate, String toDate);
}
