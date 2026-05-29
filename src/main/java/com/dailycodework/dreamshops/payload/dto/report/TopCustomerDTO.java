package com.dailycodework.dreamshops.payload.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopCustomerDTO {
    private Long customerId;
    private String customerName;
    private String customerCode;
    private Integer orderCount;
    private BigDecimal totalSpending;
    private BigDecimal avgOrderValue;
}
