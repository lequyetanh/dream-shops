package com.dailycodework.dreamshops.dto.warehouseTransaction;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WarehouseTransactionDetailReq {
    private Long id;
    private Long warehouseTransactionId;
    private Long productId;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;
}
