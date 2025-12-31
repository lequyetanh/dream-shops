package com.dailycodework.dreamshops.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Cart {
    private Long id;
    private BigDecimal totalAmount;
}
