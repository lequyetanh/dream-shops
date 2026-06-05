package com.dailycodework.dreamshops.payload.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductInfo {
    private Long id;
    private String name;
    private String description;
    private String barcode;
    private String image;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private Long companyId;
    private Integer stockQuantity;

    private List<Long> categoryIds;
}
