package com.dailycodework.dreamshops.service.report;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.report.RevenueComparisonDTO;
import com.dailycodework.dreamshops.repository.report.ReportRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final ReportRepositoryCustom reportRepository;

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

        RevenueComparisonDTO result = new RevenueComparisonDTO(
                period1Total, period2Total,
                period1OrderCount, period2OrderCount,
                growthAmount, growthPercent,
                period1Data, period2Data
        );
        return new BaseResultDTO(ResultNotify.successGet, true, result);
    }
}
