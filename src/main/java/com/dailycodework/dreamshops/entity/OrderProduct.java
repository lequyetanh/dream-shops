package com.dailycodework.dreamshops.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_product")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "product_name")
    private String productName;
    private BigDecimal price;
    private BigDecimal quantity;
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;
    @Column(name = "vat_rate")
    private Integer vatRate;
    @Column(name = "vat_amount")
    private BigDecimal vatAmount;
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Order order;
}
