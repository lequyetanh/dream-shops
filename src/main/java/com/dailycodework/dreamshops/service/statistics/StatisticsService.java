package com.dailycodework.dreamshops.service.statistics;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.statistics.DashboardDTO;
import com.dailycodework.dreamshops.payload.dto.statistics.OrderStatusCountDTO;
import com.dailycodework.dreamshops.payload.dto.statistics.RevenueStatDTO;
import com.dailycodework.dreamshops.payload.dto.statistics.TopProductDTO;
import com.dailycodework.dreamshops.repository.statistics.StatisticsRepositoryCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Transactional
public class StatisticsService implements IStatisticsService {

    private final StatisticsRepositoryCustom statisticsRepository;

    // Thread pool riêng cho các tác vụ thống kê bất đồng bộ
    private final Executor statsExecutor = Executors.newFixedThreadPool(3);

    @Override
    public BaseResultDTO getRevenue(String fromDate, String toDate, String groupBy, Long companyId) {
        var result = statisticsRepository.getRevenue(fromDate, toDate, groupBy, companyId);
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }

    @Override
    public BaseResultDTO getTopProducts(Long companyId, String fromDate, String toDate, Integer limit) {
        int topLimit = (limit != null && limit > 0) ? limit : 10;
        var result = statisticsRepository.getTopProducts(companyId, fromDate, toDate, topLimit);
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }

    @Override
    public BaseResultDTO getOrderStatusCount(Long companyId, String fromDate, String toDate) {
        var result = statisticsRepository.getOrderStatusCount(companyId, fromDate, toDate);
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }

    /**
     * Gọi 3 query thống kê SONG SONG thay vì tuần tự.
     * Thời gian phản hồi = query chậm nhất (thay vì tổng cộng 3 query).
     *
     * Ví dụ: revenue=200ms, topProducts=150ms, orderStatus=100ms
     *   - Tuần tự: 200+150+100 = 450ms
     *   - Song song: max(200,150,100) = 200ms
     */
    @Override
    public BaseResultDTO getDashboard(Long companyId, String fromDate, String toDate, String groupBy, Integer topLimit) {
        int limit = (topLimit != null && topLimit > 0) ? topLimit : 10;

        // Khởi chạy 3 tác vụ đồng thời, mỗi cái chạy trên thread riêng từ statsExecutor
        CompletableFuture<List<RevenueStatDTO>> revenueFuture = CompletableFuture.supplyAsync(
                () -> statisticsRepository.getRevenue(fromDate, toDate, groupBy, companyId),
                statsExecutor
        );

        CompletableFuture<List<TopProductDTO>> topProductsFuture = CompletableFuture.supplyAsync(
                () -> statisticsRepository.getTopProducts(companyId, fromDate, toDate, limit),
                statsExecutor
        );

        CompletableFuture<List<OrderStatusCountDTO>> orderStatusFuture = CompletableFuture.supplyAsync(
                () -> statisticsRepository.getOrderStatusCount(companyId, fromDate, toDate),
                statsExecutor
        );

        // Chờ cả 3 hoàn thành rồi gộp kết quả
        DashboardDTO dashboard = CompletableFuture
                // Chờ tất cả các Future hoàn thành.
                .allOf(revenueFuture, topProductsFuture, orderStatusFuture)
                .thenApply(ignored -> new DashboardDTO(
                        revenueFuture.join(),
                        topProductsFuture.join(),
                        orderStatusFuture.join()
                ))
                .join(); // block thread HTTP tại đây cho đến khi cả 3 xong

        return new BaseResultDTO(ResultNotify.successGet, true, dashboard);
    }
}
