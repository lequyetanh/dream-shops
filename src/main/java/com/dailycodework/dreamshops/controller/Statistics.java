package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.service.statistics.IStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class Statistics {
    private final IStatisticsService statisticsService;

    /**
     * Doanh thu theo ngày hoặc tháng
     * groupBy: "day" | "month"
     */
    @GetMapping("/revenue")
    public ResponseEntity<BaseResultDTO> getRevenue(
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false, defaultValue = "day") String groupBy,
            @RequestParam(required = false) Long companyId
    ){
        BaseResultDTO result = statisticsService.getRevenue(fromDate, toDate, groupBy, companyId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Top sản phẩm bán chạy theo doanh thu
     */
    @GetMapping("/top-products")
    public ResponseEntity<BaseResultDTO> getTopProducts(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false, defaultValue = "10") Integer limit
    ){
        BaseResultDTO result = statisticsService.getTopProducts(companyId, fromDate, toDate, limit);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Số lượng đơn hàng theo trạng thái
     */
    @GetMapping("/order-status-count")
    public ResponseEntity<BaseResultDTO> getOrderStatusCount(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ){
        BaseResultDTO result = statisticsService.getOrderStatusCount(companyId, fromDate, toDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Dashboard: gộp revenue + top-products + order-status-count trong 1 request.
     * 3 query chạy song song bằng CompletableFuture.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<BaseResultDTO> getDashboard(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false, defaultValue = "day") String groupBy,
            @RequestParam(required = false, defaultValue = "10") Integer topLimit
    ){
        BaseResultDTO result = statisticsService.getDashboard(companyId, fromDate, toDate, groupBy, topLimit);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
