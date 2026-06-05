package com.dailycodework.dreamshops.payload.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String barcode;
    private String image;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private Long companyId;
    private Integer stockQuantity;

    ProductResponse(
            Long id,
            String name,
            String description,
            String barcode,
            String image,
            BigDecimal inPrice,
            BigDecimal outPrice,
            Long companyId,
            Integer stockQuantity
    ){
        this.id = id;
        this.name = name;
        this.description = description;
        this.barcode = barcode;
        this.image = image;
        this.inPrice = inPrice;
        this.outPrice = outPrice;
        this.companyId = companyId;
        this.stockQuantity = stockQuantity;
    }
}
