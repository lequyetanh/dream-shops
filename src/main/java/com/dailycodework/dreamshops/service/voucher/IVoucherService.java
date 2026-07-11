package com.dailycodework.dreamshops.service.voucher;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.voucher.VoucherInfo;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface IVoucherService {
    BaseResultDTO getVoucherWithPaging(
            Pageable pageable,
            String keyword,
            String code,
            Integer status,
            Long companyId
    );
    BaseResultDTO findById(Long id);
    BaseResultDTO createVoucher(VoucherInfo voucherReq);
    BaseResultDTO updateVoucher(VoucherInfo voucherReq);
    BaseResultDTO updateVoucherStatus(Long id, Integer status);
    BaseResultDTO deleteVoucher(Long id);

    /**
     * Validate a voucher code against an order amount and compute the discount, without consuming a usage slot.
     */
    BaseResultDTO checkVoucher(String code, Long companyId, BigDecimal orderAmount);

    /**
     * Validate a voucher code against an order amount, compute the discount and consume one usage slot.
     */
    BaseResultDTO applyVoucher(String code, Long companyId, BigDecimal orderAmount);

    /**
     * Give back one usage slot for a previously applied voucher (order updated/deleted/cancelled).
     */
    void releaseVoucher(Long voucherId);
}
