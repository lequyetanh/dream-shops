package com.dailycodework.dreamshops.repository.report;

import com.dailycodework.dreamshops.payload.dto.report.InventoryReportDTO;
import com.dailycodework.dreamshops.payload.dto.report.RevenueByCategoryDTO;
import com.dailycodework.dreamshops.payload.dto.report.RevenueComparisonDTO;
import com.dailycodework.dreamshops.payload.dto.report.TopCustomerDTO;

import java.util.List;

public interface ReportRepositoryCustom {
    List<InventoryReportDTO> getInventoryReport(Long companyId, Long categoryId);
    List<RevenueByCategoryDTO> getRevenueByCategoryReport(Long companyId, String fromDate, String toDate);
    List<TopCustomerDTO> getTopCustomersReport(Long companyId, String fromDate, String toDate, int limit);
    List<RevenueComparisonDTO.PeriodPoint> getRevenuePeriod(Long companyId, String fromDate, String toDate, String groupBy);
}
