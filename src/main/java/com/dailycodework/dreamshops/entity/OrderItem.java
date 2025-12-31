package com.dailycodework.dreamshops.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private Long id;
    private int quantity;
    private BigDecimal price;
}
