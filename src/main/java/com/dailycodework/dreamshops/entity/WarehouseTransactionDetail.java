package com.dailycodework.dreamshops.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "warehouse_transaction_detail")
public class WarehouseTransactionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "warehouse_transaction_id")
    private Long warehouseTransactionId;
    @Column(name = "product_id")
    private Long productId;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_transaction_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private WarehouseTransaction warehouseTransaction;
}
