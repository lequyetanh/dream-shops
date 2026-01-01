package com.dailycodework.dreamshops.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String barcode;
    private String image;
    @Column(name = "in_price")
    private BigDecimal inPrice;
    @Column(name = "out_Price")
    private BigDecimal outPrice;
    @Column(name = "company_id")
    private Long companyId;
}
