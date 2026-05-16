package com.dailycodework.dreamshops.payload.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStatDTO {
    private String dateLabel;
    private BigDecimal revenue;
    private Integer orderCount;
}
