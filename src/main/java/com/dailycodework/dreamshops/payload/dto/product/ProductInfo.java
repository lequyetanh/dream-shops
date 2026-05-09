package com.dailycodework.dreamshops.payload.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductInfo {
    private Long id;
    private String name;
    private String description;
    private String barcode;
    private String image;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private Long companyId;

    private List<Long> categoryIds;
}
