package com.dailycodework.dreamshops.payload.dto.order;

import com.dailycodework.dreamshops.payload.dto.orderProduct.OrderProductReq;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderInfo {
    private Long id;
    private String code;
    private Long customerId;
    private String orderDate;
    private String description;
    private BigDecimal discountAmount;
    private Integer vatRate;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
    private Long companyId;
    private Integer status;
    private String extra;
    List<OrderProductReq> products;

    OrderInfo(
            Long id,
            String code,
            Long customerId,
            String orderDate,
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
