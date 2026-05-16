package com.dailycodework.dreamshops.payload.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueComparisonDTO {
    private BigDecimal period1Total;
    private BigDecimal period2Total;
    private Integer period1OrderCount;
    private Integer period2OrderCount;
    private BigDecimal growthAmount;    // period2 - period1
    private BigDecimal growthPercent;   // ((p2 - p1) / p1) × 100, null nếu p1 = 0

    private List<PeriodPoint> period1Data;
    private List<PeriodPoint> period2Data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PeriodPoint {
        private String dateLabel;
        private BigDecimal revenue;
        private Integer orderCount;
    }
}
