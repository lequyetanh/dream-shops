package com.dailycodework.dreamshops.dto.order;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class OrderInfo {
    private Long id;
    private String code;
    private String customerId;
    private ZonedDateTime orderDate;
    private String description;
    private BigDecimal discountAmount;
    private Integer vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private Long companyId;
    private String extra;
}
