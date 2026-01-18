package com.dailycodework.dreamshops.dto.order;

import com.dailycodework.dreamshops.dto.orderProduct.OrderProductReq;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class OrderInfo {
    private Long id;
    private String code;
    private String customerId;
    private ZonedDateTime orderDate;
    private String description;
    private BigDecimal discountAmount;
    private Integer vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private Long companyId;
    private String extra;
    List<OrderProductReq> orderProductList;

    OrderInfo(
            Long id,
            String code,
            String customerId,
            ZonedDateTime orderDate,
            String description,
            BigDecimal discountAmount,
            Integer vatRate,
            BigDecimal vatAmount,
            BigDecimal totalAmount,
            Long companyId,
            String extra
    ){
        this.id = id;
        this.code = code;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.description = description;
        this.discountAmount = discountAmount;
        this.vatRate = vatRate;
        this.vatAmount = vatAmount;
        this.totalAmount = totalAmount;
        this.companyId = companyId;
        this.extra = extra;
    }
}
