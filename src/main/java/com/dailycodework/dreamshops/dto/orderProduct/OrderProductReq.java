package com.dailycodework.dreamshops.dto.orderProduct;


import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductReq {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal discountAmount;
    private Integer vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalPrice;
}
