package com.dailycodework.dreamshops.entity;

import com.dailycodework.dreamshops.constant.BaseConstant;
import com.dailycodework.dreamshops.util.Common;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Size(max = 100)
    @Column(length = 100)
    private String no;

    @Column(nullable = false)
    private ZonedDateTime date;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @PositiveOrZero
    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Min(0) @Max(100)
    @Column(name = "vat_rate")
    private Integer vatRate;

    @Min(0)
    @Column(name = "vat_amount")
    private Integer vatAmount;

    @PositiveOrZero
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @JsonManagedReference
    @OneToMany(mappedBy = "warehouseTransaction", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<WarehouseTransactionDetail> warehouseTransactionDetail;

    public void setWarehouseTransactionDate(String warehouseTransactionDate) {
        ZonedDateTime warehouseTransactionDateConvert = Common.convertStringToZoneDateTime(warehouseTransactionDate, BaseConstant.ZONED_DATE_TIME_FORMAT);
        this.date = warehouseTransactionDateConvert;
    }
}
