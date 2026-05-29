package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
