package com.dailycodework.dreamshops.entity;

import com.dailycodework.dreamshops.constant.BaseConstant;
import com.dailycodework.dreamshops.payload.dto.voucher.VoucherInfo;
import com.dailycodework.dreamshops.util.Common;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "voucher")
@SqlResultSetMappings(
        {
                @SqlResultSetMapping(
                        name = "VoucherResponse",
                        classes = {
                                @ConstructorResult(
                                        targetClass = VoucherInfo.class,
                                        columns = {
                                                @ColumnResult(name = "id", type = Long.class),
                                                @ColumnResult(name = "code", type = String.class),
                                                @ColumnResult(name = "name", type = String.class),
                                                @ColumnResult(name = "description", type = String.class),
                                                @ColumnResult(name = "discountType", type = Integer.class),
                                                @ColumnResult(name = "discountValue", type = BigDecimal.class),
                                                @ColumnResult(name = "minOrderAmount", type = BigDecimal.class),
                                                @ColumnResult(name = "maxDiscountAmount", type = BigDecimal.class),
                                                @ColumnResult(name = "startDate", type = ZonedDateTime.class),
                                                @ColumnResult(name = "endDate", type = ZonedDateTime.class),
                                                @ColumnResult(name = "usageLimit", type = Integer.class),
                                                @ColumnResult(name = "usedCount", type = Integer.class),
                                                @ColumnResult(name = "status", type = Integer.class),
                                                @ColumnResult(name = "companyId", type = Long.class),
                                                @ColumnResult(name = "extra", type = String.class)
                                        }
                                )
                        }
                )
        }
)
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50, unique = true)
    private String code;

    @Size(max = 255)
    @Column(length = 255)
    private String name;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @NotNull
    @Column(name = "discount_type", nullable = false)
    private Integer discountType;

    @NotNull
    @PositiveOrZero
    @Column(name = "discount_value", nullable = false, precision = 19, scale = 2)
    private BigDecimal discountValue;

    @PositiveOrZero
    @Column(name = "min_order_amount", precision = 19, scale = 2)
    private BigDecimal minOrderAmount;

    @PositiveOrZero
    @Column(name = "max_discount_amount", precision = 19, scale = 2)
    private BigDecimal maxDiscountAmount;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @PositiveOrZero
    @Column(name = "usage_limit")
    private Integer usageLimit;

    @PositiveOrZero
    @Column(name = "used_count")
    private Integer usedCount = 0;

    @Min(0)
    private Integer status;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String extra;

    public void setStartDate(String startDate) {
        this.startDate = Common.convertStringToZoneDateTime(startDate, BaseConstant.ZONED_DATE_TIME_FORMAT);
    }

    public void setEndDate(String endDate) {
        this.endDate = Common.convertStringToZoneDateTime(endDate, BaseConstant.ZONED_DATE_TIME_FORMAT);
    }
}
