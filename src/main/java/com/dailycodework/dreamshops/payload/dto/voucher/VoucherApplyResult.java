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
public class VoucherApplyResult {
    private Long voucherId;
    private String code;
    private Integer discountType;
    private BigDecimal discountAmount;
}
