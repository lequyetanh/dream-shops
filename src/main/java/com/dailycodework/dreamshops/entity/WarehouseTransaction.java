package com.dailycodework.dreamshops.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "warehouse_transaction")
public class WarehouseTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "company_id")
    private Integer companyId;
    private String no;
    private ZonedDateTime date;
    private String description;
    private BigDecimal amount;
    @Column(name = "vat_rate")
    private Integer vatRate;
    @Column(name = "vat_amount")
    private Integer vatAmount;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @JsonManagedReference
    @OneToMany(mappedBy = "warehouse_transaction", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<WarehouseTransaction> warehouseTransactionDetail;

}
