package com.dailycodework.dreamshops.entity;

import com.dailycodework.dreamshops.constant.BaseConstant;
import com.dailycodework.dreamshops.payload.dto.order.OrderInfo;
import com.dailycodework.dreamshops.util.Common;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

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
                                                @ColumnResult(name = "extra", type = String.class),
                                                @ColumnResult(name = "voucherId", type = Long.class),
                                                @ColumnResult(name = "voucherCode", type = String.class)
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

    @Size(max = 100)
    @Column(length = 100)
    private String code;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "order_date", nullable = false)
    private ZonedDateTime orderDate;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @PositiveOrZero
    @Column(name = "discount_amount", precision = 19, scale = 2)
    private BigDecimal discountAmount;

    @Min(0) @Max(100)
    @Column(name = "vat_rate")
    private Integer vatRate;

    @PositiveOrZero
    @Column(name = "vat_amount", precision = 19, scale = 2)
    private BigDecimal vatAmount;

    @PositiveOrZero
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Min(0)
    private Integer status;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String extra;

    @Column(name = "voucher_id")
    private Long voucherId;

    @Size(max = 50)
    @Column(name = "voucher_code", length = 50)
    private String voucherCode;

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
        Integer normDate = Common.normalizedTime(orderDate, BaseConstant.NORMALIZED_DATE_FORMAT);
    }

    public void setOrderDate(String orderDate) {
        ZonedDateTime orderDateConvert = Common.convertStringToZoneDateTime(orderDate, BaseConstant.ZONED_DATE_TIME_FORMAT);
        this.orderDate = orderDateConvert;
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderProduct> products;
}
