package com.dailycodework.dreamshops.service.statistics;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;

public interface IStatisticsService {
    BaseResultDTO getRevenue(String fromDate, String toDate, String groupBy, Long companyId);
    BaseResultDTO getTopProducts(Long companyId, String fromDate, String toDate, Integer limit);
    BaseResultDTO getOrderStatusCount(Long companyId, String fromDate, String toDate);
}
