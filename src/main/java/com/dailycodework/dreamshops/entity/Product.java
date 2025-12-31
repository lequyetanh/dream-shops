package com.dailycodework.dreamshops.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
}
