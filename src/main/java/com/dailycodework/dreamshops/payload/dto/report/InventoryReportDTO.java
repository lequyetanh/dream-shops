package com.dailycodework.dreamshops.payload.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReportDTO {
    private Long productId;
    private String productName;
    private String barcode;
    private Integer stockQuantity;
    private BigDecimal inPrice;
    private BigDecimal stockValue;      // stockQuantity × inPrice
    private String categoryNames;       // danh mục, phân cách bởi dấu phẩy
}
