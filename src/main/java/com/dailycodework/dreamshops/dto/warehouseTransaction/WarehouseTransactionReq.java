package com.dailycodework.dreamshops.dto.warehouseTransaction;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class WarehouseTransactionReq {
    private Long id;
    private String no;
    private String description;
    private ZonedDateTime date;
    private BigDecimal amount;
    private Integer companyId;
    private Integer vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private List<WarehouseTransactionDetailReq> details;
}
