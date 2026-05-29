package com.dailycodework.dreamshops.payload.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueByCategoryDTO {
    private Long categoryId;
    private String categoryName;
    private Integer orderCount;
    private BigDecimal totalRevenue;
}
