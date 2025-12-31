package com.dailycodework.dreamshops.entity;

import com.dailycodework.dreamshops.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Order {
    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
}
