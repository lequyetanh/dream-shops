package com.dailycodework.dreamshops.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "order_date")
    private ZonedDateTime  orderDate;
    private String description;
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;
    @Column(name = "vat_rate")
    private Integer vatRate;
    @Column(name = "vat_amount")
    private BigDecimal vatAmount;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @Column(name = "company_id")
    private Long companyId;
}
