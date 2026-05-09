package com.dailycodework.dreamshops.entity;

import com.dailycodework.dreamshops.constant.BaseConstant;
import com.dailycodework.dreamshops.payload.dto.order.OrderInfo;
import com.dailycodework.dreamshops.util.Common;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
@SqlResultSetMappings(
        {
                @SqlResultSetMapping(
                        name = "OrderResponse",
                        classes = {
                                @ConstructorResult(
                                        targetClass = OrderInfo.class,
                                        columns = {
                                                @ColumnResult(name = "id", type = Long.class),
                                                @ColumnResult(name = "code", type = String.class),
                                                @ColumnResult(name = "customerId", type = Long.class),
                                                @ColumnResult(name = "orderDate", type = ZonedDateTime.class),
                                                @ColumnResult(name = "description", type = String.class),
                                                @ColumnResult(name = "discountAmount", type = BigDecimal.class),
                                                @ColumnResult(name = "vatRate", type = Integer.class),
                                                @ColumnResult(name = "vatAmount", type = BigDecimal.class),
                                                @ColumnResult(name = "totalAmount", type = BigDecimal.class),
                                                @ColumnResult(name = "companyId", type = Long.class),
                                                @ColumnResult(name = "status", type = Integer.class),
                                                @ColumnResult(name = "extra", type = String.class)
                                        }
                                )
                        }
                )
        }
)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(name = "customer_id")
    private Long customerId;
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
    private Integer status;
    private String extra;

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
        Integer normDate = Common.normalizedTime(orderDate, BaseConstant.NORMALIZED_DATE_FORMAT);
    }

    public void setOrderDate(String orderDate) {
        ZonedDateTime orderDateConvert = Common.convertStringToZoneDateTime(orderDate, BaseConstant.ZONED_DATE_TIME_FORMAT);
        this.orderDate = orderDateConvert;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderProduct> products;


}
