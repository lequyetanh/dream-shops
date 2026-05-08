package com.dailycodework.dreamshops.dto.warehouseTransaction;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class WarehouseTransactionList {
        private Long id;
        private String no;
        private String description;
        private ZonedDateTime date;
        private BigDecimal amount;
        private Integer companyId;
        private Integer vatRate;
        private BigDecimal vatAmount;
        private BigDecimal totalAmount;
}
