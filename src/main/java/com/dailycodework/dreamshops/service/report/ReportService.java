package com.dailycodework.dreamshops.service.report;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.report.RevenueComparisonDTO;
import com.dailycodework.dreamshops.repository.report.ReportRepositoryCustom;
import com.dailycodework.dreamshops.util.Common;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService implements IReportService {
    private static final List<ReportColumn> INVENTORY_COLUMNS = List.of(
            new ReportColumn("Mã SP", "productId", 60),
            new ReportColumn("Tên sản phẩm", "productName", 160),
            new ReportColumn("Mã vạch", "barcode", 90),
            new ReportColumn("Tồn kho", "stockQuantity", 60),
            new ReportColumn("Giá nhập", "inPrice", 90),
            new ReportColumn("Giá trị tồn", "stockValue", 100),
            new ReportColumn("Danh mục", "categoryNames", 170)
    );
    private static final List<ReportColumn> REVENUE_BY_CATEGORY_COLUMNS = List.of(
            new ReportColumn("Mã danh mục", "categoryId", 80),
            new ReportColumn("Tên danh mục", "categoryName", 200),
            new ReportColumn("Số đơn", "orderCount", 100),
            new ReportColumn("Doanh thu", "totalRevenue", 150)
    );
    private static final List<ReportColumn> TOP_CUSTOMERS_COLUMNS = List.of(
            new ReportColumn("Mã KH", "customerId", 70),
            new ReportColumn("Tên khách hàng", "customerName", 180),
            new ReportColumn("Mã khách hàng", "customerCode", 100),
            new ReportColumn("Số đơn", "orderCount", 70),
            new ReportColumn("Tổng chi tiêu", "totalSpending", 130),
            new ReportColumn("TB/đơn", "avgOrderValue", 130)
    );
    private static final List<ReportColumn> REVENUE_COMPARISON_COLUMNS = List.of(
            new ReportColumn("Kỳ", "period", 60),
            new ReportColumn("Ngày/Tháng", "dateLabel", 130),
            new ReportColumn("Doanh thu", "revenue", 150),
            new ReportColumn("Số đơn", "orderCount", 100)
    );

    private final ReportRepositoryCustom reportRepository;
    private final ReportExcelExportService reportExcelExportService;
    private final ReportPdfExportService reportPdfExportService;

    @Override
    public BaseResultDTO getInventoryReport(Long companyId, Long categoryId) {
        var result = reportRepository.getInventoryReport(companyId, categoryId);
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }

    @Override
    public BaseResultDTO getRevenueByCategoryReport(Long companyId, String fromDate, String toDate) {
        var result = reportRepository.getRevenueByCategoryReport(companyId, fromDate, toDate);
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }

    @Override
    public BaseResultDTO getTopCustomersReport(Long companyId, String fromDate, String toDate, Integer limit) {
        int top = (limit != null && limit > 0) ? limit : 10;
        var result = reportRepository.getTopCustomersReport(companyId, fromDate, toDate, top);
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }

    @Override
    public BaseResultDTO compareRevenue(Long companyId, String from1, String to1, String from2, String to2, String groupBy) {
        RevenueComparisonDTO result = buildRevenueComparison(companyId, from1, to1, from2, to2, groupBy);
        return new BaseResultDTO(ResultNotify.successGet, true, result);
    }

    @Override
    public byte[] exportInventoryReportToExcel(Long companyId, Long categoryId) throws IOException {
        var data = reportRepository.getInventoryReport(companyId, categoryId);
        return reportExcelExportService.export("Báo cáo tồn kho", null, INVENTORY_COLUMNS, toRows(data));
    }

    @Override
    public byte[] exportInventoryReportToPdf(Long companyId, Long categoryId) throws JRException {
        var data = reportRepository.getInventoryReport(companyId, categoryId);
        return reportPdfExportService.export("Báo cáo tồn kho", null, INVENTORY_COLUMNS, toRows(data));
    }

    @Override
    public byte[] exportRevenueByCategoryToExcel(Long companyId, String fromDate, String toDate) throws IOException {
        var data = reportRepository.getRevenueByCategoryReport(companyId, fromDate, toDate);
        return reportExcelExportService.export("Báo cáo doanh thu theo danh mục", null, REVENUE_BY_CATEGORY_COLUMNS, toRows(data));
    }

    @Override
    public byte[] exportRevenueByCategoryToPdf(Long companyId, String fromDate, String toDate) throws JRException {
        var data = reportRepository.getRevenueByCategoryReport(companyId, fromDate, toDate);
        return reportPdfExportService.export("Báo cáo doanh thu theo danh mục", null, REVENUE_BY_CATEGORY_COLUMNS, toRows(data));
    }

    @Override
    public byte[] exportTopCustomersToExcel(Long companyId, String fromDate, String toDate, Integer limit) throws IOException {
        int top = (limit != null && limit > 0) ? limit : 10;
        var data = reportRepository.getTopCustomersReport(companyId, fromDate, toDate, top);
        return reportExcelExportService.export("Báo cáo top khách hàng", null, TOP_CUSTOMERS_COLUMNS, toRows(data));
    }

    @Override
    public byte[] exportTopCustomersToPdf(Long companyId, String fromDate, String toDate, Integer limit) throws JRException {
        int top = (limit != null && limit > 0) ? limit : 10;
        var data = reportRepository.getTopCustomersReport(companyId, fromDate, toDate, top);
        return reportPdfExportService.export("Báo cáo top khách hàng", null, TOP_CUSTOMERS_COLUMNS, toRows(data));
    }

    @Override
    public byte[] exportRevenueComparisonToExcel(
            Long companyId, String from1, String to1, String from2, String to2, String groupBy
    ) throws IOException {
        RevenueComparisonDTO dto = buildRevenueComparison(companyId, from1, to1, from2, to2, groupBy);
        return reportExcelExportService.export(
                "So sánh doanh thu", buildComparisonSubtitle(dto), REVENUE_COMPARISON_COLUMNS, flattenRevenueComparison(dto)
        );
    }

    @Override
    public byte[] exportRevenueComparisonToPdf(
            Long companyId, String from1, String to1, String from2, String to2, String groupBy
    ) throws JRException {
        RevenueComparisonDTO dto = buildRevenueComparison(companyId, from1, to1, from2, to2, groupBy);
        return reportPdfExportService.export(
                "So sánh doanh thu", buildComparisonSubtitle(dto), REVENUE_COMPARISON_COLUMNS, flattenRevenueComparison(dto)
        );
    }

    private RevenueComparisonDTO buildRevenueComparison(
            Long companyId, String from1, String to1, String from2, String to2, String groupBy
    ) {
        List<RevenueComparisonDTO.PeriodPoint> period1Data = reportRepository.getRevenuePeriod(companyId, from1, to1, groupBy);
        List<RevenueComparisonDTO.PeriodPoint> period2Data = reportRepository.getRevenuePeriod(companyId, from2, to2, groupBy);

        BigDecimal period1Total = period1Data.stream()
                .map(RevenueComparisonDTO.PeriodPoint::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal period2Total = period2Data.stream()
                .map(RevenueComparisonDTO.PeriodPoint::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int period1OrderCount = period1Data.stream()
                .mapToInt(RevenueComparisonDTO.PeriodPoint::getOrderCount).sum();

        int period2OrderCount = period2Data.stream()
                .mapToInt(RevenueComparisonDTO.PeriodPoint::getOrderCount).sum();

        BigDecimal growthAmount = period2Total.subtract(period1Total);
        BigDecimal growthPercent = null;
        if (period1Total.compareTo(BigDecimal.ZERO) != 0) {
            growthPercent = growthAmount
                    .divide(period1Total, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return new RevenueComparisonDTO(
                period1Total, period2Total,
                period1OrderCount, period2OrderCount,
                growthAmount, growthPercent,
                period1Data, period2Data
        );
    }

    private List<Map<String, Object>> flattenRevenueComparison(RevenueComparisonDTO dto) {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (RevenueComparisonDTO.PeriodPoint point : dto.getPeriod1Data()) {
            Map<String, Object> row = toRow(point);
            row.put("period", "Kỳ 1");
            rows.add(row);
        }
        for (RevenueComparisonDTO.PeriodPoint point : dto.getPeriod2Data()) {
            Map<String, Object> row = toRow(point);
            row.put("period", "Kỳ 2");
            rows.add(row);
        }
        return rows;
    }

    private String buildComparisonSubtitle(RevenueComparisonDTO dto) {
        String growth = dto.getGrowthPercent() != null ? dto.getGrowthPercent() + "%" : "N/A";
        return String.format(
                "Kỳ 1: %s (%d đơn)   |   Kỳ 2: %s (%d đơn)   |   Tăng trưởng: %s (%s)",
                formatMoney(dto.getPeriod1Total()), dto.getPeriod1OrderCount(),
                formatMoney(dto.getPeriod2Total()), dto.getPeriod2OrderCount(),
                formatMoney(dto.getGrowthAmount()), growth
        );
    }

    private String formatMoney(BigDecimal amount) {
        return amount != null ? amount.toPlainString() : "0";
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> toRows(List<?> items) {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Object item : items) {
            rows.add(toRow(item));
        }
        return rows;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> toRow(Object item) {
        return Common.fromJsonString(Common.toJsonString(item), Map.class);
    }
}
