package com.dailycodework.dreamshops.service.report;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface IReportService {
    BaseResultDTO getInventoryReport(Long companyId, Long categoryId);
    BaseResultDTO getRevenueByCategoryReport(Long companyId, String fromDate, String toDate);
    BaseResultDTO getTopCustomersReport(Long companyId, String fromDate, String toDate, Integer limit);
    BaseResultDTO compareRevenue(Long companyId, String from1, String to1, String from2, String to2, String groupBy);

    byte[] exportInventoryReportToExcel(Long companyId, Long categoryId) throws IOException;
    byte[] exportInventoryReportToPdf(Long companyId, Long categoryId) throws JRException;

    byte[] exportRevenueByCategoryToExcel(Long companyId, String fromDate, String toDate) throws IOException;
    byte[] exportRevenueByCategoryToPdf(Long companyId, String fromDate, String toDate) throws JRException;

    byte[] exportTopCustomersToExcel(Long companyId, String fromDate, String toDate, Integer limit) throws IOException;
    byte[] exportTopCustomersToPdf(Long companyId, String fromDate, String toDate, Integer limit) throws JRException;

    byte[] exportRevenueComparisonToExcel(
            Long companyId, String from1, String to1, String from2, String to2, String groupBy
    ) throws IOException;
    byte[] exportRevenueComparisonToPdf(
            Long companyId, String from1, String to1, String from2, String to2, String groupBy
    ) throws JRException;
}
