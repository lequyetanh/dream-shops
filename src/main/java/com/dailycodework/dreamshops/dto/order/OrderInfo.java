package com.dailycodework.dreamshops.dto.order;

import com.dailycodework.dreamshops.dto.orderProduct.OrderProductReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {
    private Long id;
    private String code;
    private Long customerId;
    private ZonedDateTime orderDate;
    private String description;
    private BigDecimal discountAmount;
    private Integer vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private Long companyId;
    private Integer status;
    private String extra;
    List<OrderProductReq> orderProductList;

    OrderInfo(
            Long id,
            String code,
            Long customerId,
            ZonedDateTime orderDate,
            String description,
            BigDecimal discountAmount,
            Integer vatRate,
            BigDecimal vatAmount,
            BigDecimal totalAmount,
            Long companyId,
            Integer status,
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
        this.status = status;
        this.extra = extra;
    }
}
