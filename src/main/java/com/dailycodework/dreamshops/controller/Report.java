package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class Report {
    private final ReportService reportService;

    /**
     * Báo cáo tồn kho sản phẩm
     * Trả về: productId, name, barcode, stockQuantity, inPrice, stockValue, categoryNames
     * Sắp xếp theo stockQuantity tăng dần (sản phẩm sắp hết hàng lên đầu)
     */
    @GetMapping("/inventory")
    public ResponseEntity<BaseResultDTO> getInventoryReport(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long categoryId
    ){
        BaseResultDTO result = reportService.getInventoryReport(companyId, categoryId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Báo cáo doanh thu theo danh mục sản phẩm
     * Trả về: categoryId, categoryName, orderCount, totalRevenue
     * Sắp xếp theo totalRevenue giảm dần
     */
    @GetMapping("/revenue-by-category")
    public ResponseEntity<BaseResultDTO> getRevenueByCategoryReport(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ){
        BaseResultDTO result = reportService.getRevenueByCategoryReport(companyId, fromDate, toDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Báo cáo top khách hàng theo tổng chi tiêu
     * Trả về: customerId, name, code, orderCount, totalSpending, avgOrderValue
     */
    @GetMapping("/top-customers")
    public ResponseEntity<BaseResultDTO> getTopCustomersReport(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ){
        BaseResultDTO result = reportService.getTopCustomersReport(companyId, fromDate, toDate, limit);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * So sánh doanh thu giữa 2 kỳ
     * Trả về: tổng 2 kỳ, tăng/giảm (số tiền + %), chi tiết từng ngày/tháng của cả 2 kỳ
     * groupBy: "day" | "month"
     */
    @GetMapping("/revenue-comparison")
    public ResponseEntity<BaseResultDTO> compareRevenue(
            @RequestParam(required = false) Long companyId,
            @RequestParam String from1,
            @RequestParam String to1,
            @RequestParam String from2,
            @RequestParam String to2,
            @RequestParam(required = false, defaultValue = "day") String groupBy
    ){
        BaseResultDTO result = reportService.compareRevenue(companyId, from1, to1, from2, to2, groupBy);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Xuất báo cáo tồn kho ra Excel
    @GetMapping("/inventory/export-excel")
    public ResponseEntity<byte[]> exportInventoryToExcel(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long categoryId
    ) throws IOException {
        byte[] content = reportService.exportInventoryReportToExcel(companyId, categoryId);
        return buildFileResponse(content, "bao-cao-ton-kho.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // Xuất báo cáo tồn kho ra PDF (JasperReports)
    @GetMapping("/inventory/export-pdf")
    public ResponseEntity<byte[]> exportInventoryToPdf(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long categoryId
    ) throws JRException {
        byte[] content = reportService.exportInventoryReportToPdf(companyId, categoryId);
        return buildFileResponse(content, "bao-cao-ton-kho.pdf", MediaType.APPLICATION_PDF_VALUE);
    }

    // Xuất báo cáo doanh thu theo danh mục ra Excel
    @GetMapping("/revenue-by-category/export-excel")
    public ResponseEntity<byte[]> exportRevenueByCategoryToExcel(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ) throws IOException {
        byte[] content = reportService.exportRevenueByCategoryToExcel(companyId, fromDate, toDate);
        return buildFileResponse(content, "bao-cao-doanh-thu-danh-muc.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // Xuất báo cáo doanh thu theo danh mục ra PDF (JasperReports)
    @GetMapping("/revenue-by-category/export-pdf")
    public ResponseEntity<byte[]> exportRevenueByCategoryToPdf(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ) throws JRException {
        byte[] content = reportService.exportRevenueByCategoryToPdf(companyId, fromDate, toDate);
        return buildFileResponse(content, "bao-cao-doanh-thu-danh-muc.pdf", MediaType.APPLICATION_PDF_VALUE);
    }

    // Xuất báo cáo top khách hàng ra Excel
    @GetMapping("/top-customers/export-excel")
    public ResponseEntity<byte[]> exportTopCustomersToExcel(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) throws IOException {
        byte[] content = reportService.exportTopCustomersToExcel(companyId, fromDate, toDate, limit);
        return buildFileResponse(content, "bao-cao-top-khach-hang.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // Xuất báo cáo top khách hàng ra PDF (JasperReports)
    @GetMapping("/top-customers/export-pdf")
    public ResponseEntity<byte[]> exportTopCustomersToPdf(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ) throws JRException {
        byte[] content = reportService.exportTopCustomersToPdf(companyId, fromDate, toDate, limit);
        return buildFileResponse(content, "bao-cao-top-khach-hang.pdf", MediaType.APPLICATION_PDF_VALUE);
    }

    // Xuất báo cáo so sánh doanh thu ra Excel
    @GetMapping("/revenue-comparison/export-excel")
    public ResponseEntity<byte[]> exportRevenueComparisonToExcel(
            @RequestParam(required = false) Long companyId,
            @RequestParam String from1,
            @RequestParam String to1,
            @RequestParam String from2,
            @RequestParam String to2,
            @RequestParam(required = false, defaultValue = "day") String groupBy
    ) throws IOException {
        byte[] content = reportService.exportRevenueComparisonToExcel(companyId, from1, to1, from2, to2, groupBy);
        return buildFileResponse(content, "so-sanh-doanh-thu.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // Xuất báo cáo so sánh doanh thu ra PDF (JasperReports)
    @GetMapping("/revenue-comparison/export-pdf")
    public ResponseEntity<byte[]> exportRevenueComparisonToPdf(
            @RequestParam(required = false) Long companyId,
            @RequestParam String from1,
            @RequestParam String to1,
            @RequestParam String from2,
            @RequestParam String to2,
            @RequestParam(required = false, defaultValue = "day") String groupBy
    ) throws JRException {
        byte[] content = reportService.exportRevenueComparisonToPdf(companyId, from1, to1, from2, to2, groupBy);
        return buildFileResponse(content, "so-sanh-doanh-thu.pdf", MediaType.APPLICATION_PDF_VALUE);
    }

    private ResponseEntity<byte[]> buildFileResponse(byte[] content, String filename, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        headers.set(HttpHeaders.CONTENT_TYPE, contentType);
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
