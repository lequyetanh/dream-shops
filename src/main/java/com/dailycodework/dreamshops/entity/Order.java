package com.dailycodework.dreamshops.entity;

import com.dailycodework.dreamshops.constant.BaseConstant;
import com.dailycodework.dreamshops.util.Common;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "order_date")
    private ZonedDateTime  orderDate;
    private String description;
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;
    @Column(name = "vat_rate")
    private Integer vatRate;
    @Column(name = "vat_amount")
    private BigDecimal vatAmount;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @Column(name = "company_id")
    private Long companyId;
    private Integer Status;
    private String extra;

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
        Integer normDate = Common.normalizedTime(orderDate, BaseConstant.NORMALIZED_DATE_FORMAT);
    }

    public void setBillDate(String orderDate) {
        ZonedDateTime orderDateConvert = Common.convertStringToZoneDateTime(orderDate, BaseConstant.ZONED_DATE_TIME_FORMAT);
        this.orderDate = orderDateConvert;
    }
}
