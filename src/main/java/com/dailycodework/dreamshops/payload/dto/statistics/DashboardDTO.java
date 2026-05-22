package com.dailycodework.dreamshops.payload.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashboardDTO {
    private List<RevenueStatDTO> revenue;
    private List<TopProductDTO> topProducts;
    private List<OrderStatusCountDTO> orderStatusCount;
}
