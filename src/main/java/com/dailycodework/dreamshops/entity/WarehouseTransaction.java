package com.dailycodework.dreamshops.entity;

import com.dailycodework.dreamshops.constant.BaseConstant;
import com.dailycodework.dreamshops.util.Common;
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
    @OneToMany(mappedBy = "warehouseTransaction", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<WarehouseTransactionDetail> warehouseTransactionDetail;

    public void setWarehouseTransactionDate(String warehouseTransactionDate) {
        ZonedDateTime warehouseTransactionDateConvert = Common.convertStringToZoneDateTime(warehouseTransactionDate, BaseConstant.ZONED_DATE_TIME_FORMAT);
        this.date = warehouseTransactionDateConvert;
    }

}
