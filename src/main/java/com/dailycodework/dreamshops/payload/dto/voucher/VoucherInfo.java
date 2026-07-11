package com.dailycodework.dreamshops.payload.dto.voucher;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherInfo {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Integer discountType;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscountAmount;
    private String startDate;
    private String endDate;
    private Integer usageLimit;
    private Integer usedCount;
    private Integer status;
    private Long companyId;
    private String extra;
}
