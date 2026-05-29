package com.dailycodework.dreamshops.service.report;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;

public interface IReportService {
    BaseResultDTO getInventoryReport(Long companyId, Long categoryId);
    BaseResultDTO getRevenueByCategoryReport(Long companyId, String fromDate, String toDate);
    BaseResultDTO getTopCustomersReport(Long companyId, String fromDate, String toDate, Integer limit);
    BaseResultDTO compareRevenue(Long companyId, String from1, String to1, String from2, String to2, String groupBy);
}
