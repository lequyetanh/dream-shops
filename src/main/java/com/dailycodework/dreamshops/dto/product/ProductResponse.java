package com.dailycodework.dreamshops.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String barcode;
    private String image;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private Long companyId;

    ProductResponse(
            Long id,
            String name,
            String description,
            String barcode,
            String image,
            BigDecimal inPrice,
            BigDecimal outPrice,
            Long companyId
    ){
        this.id = id;
        this.name = name;
        this.description = description;
        this.barcode = barcode;
        this.image = image;
        this.inPrice = inPrice;
        this.outPrice = outPrice;
        this.companyId = companyId;
    }
}
