package com.dailycodework.dreamshops.service.voucher;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.constant.VoucherConstant;
import com.dailycodework.dreamshops.entity.Voucher;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.voucher.VoucherApplyResult;
import com.dailycodework.dreamshops.payload.dto.voucher.VoucherInfo;
import com.dailycodework.dreamshops.repository.voucher.IVoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VoucherService implements IVoucherService {
    private final IVoucherRepository voucherRepository;

    @Override
    @Transactional(readOnly = true)
    public BaseResultDTO getVoucherWithPaging(
            Pageable pageable,
            String keyword,
            String code,
            Integer status,
            Long companyId
    ) {
        Page<VoucherInfo> voucherList = voucherRepository.getVoucherWithPaging(pageable, keyword, code, status, companyId);
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                voucherList.getContent(),
                (int) voucherList.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResultDTO findById(Long id) {
        Optional<Voucher> voucher = voucherRepository.findById(id);
        return voucher.map(value -> new BaseResultDTO(ResultNotify.successGet, true, value))
                .orElseGet(() -> new BaseResultDTO(ResultNotify.notFound, false, null));
    }

    @Override
    public BaseResultDTO createVoucher(VoucherInfo voucherReq) {
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherReq, voucher);
        voucher.setStartDate(voucherReq.getStartDate());
        voucher.setEndDate(voucherReq.getEndDate());
        voucher.setUsedCount(0);
        voucherRepository.save(voucher);
        return new BaseResultDTO(ResultNotify.successCreate, true, voucher);
    }

    @Override
    public BaseResultDTO updateVoucher(VoucherInfo voucherReq) {
        Optional<Voucher> existingVoucherOpt = voucherRepository.findById(voucherReq.getId());
        if (existingVoucherOpt.isEmpty()) {
            return new BaseResultDTO(ResultNotify.notFound, false, null);
        }
        Voucher voucher = existingVoucherOpt.get();
        voucher.setCode(voucherReq.getCode());
        voucher.setName(voucherReq.getName());
        voucher.setDescription(voucherReq.getDescription());
        voucher.setDiscountType(voucherReq.getDiscountType());
        voucher.setDiscountValue(voucherReq.getDiscountValue());
        voucher.setMinOrderAmount(voucherReq.getMinOrderAmount());
        voucher.setMaxDiscountAmount(voucherReq.getMaxDiscountAmount());
        voucher.setStartDate(voucherReq.getStartDate());
        voucher.setEndDate(voucherReq.getEndDate());
        voucher.setUsageLimit(voucherReq.getUsageLimit());
        voucher.setStatus(voucherReq.getStatus());
        voucher.setExtra(voucherReq.getExtra());
        voucherRepository.save(voucher);
        return new BaseResultDTO(ResultNotify.successUpdate, true, voucher);
    }

    @Override
    public BaseResultDTO updateVoucherStatus(Long id, Integer status) {
        Optional<Voucher> existingVoucherOpt = voucherRepository.findById(id);
        if (existingVoucherOpt.isEmpty()) {
            return new BaseResultDTO(ResultNotify.notFound, false, null);
        }
        Voucher voucher = existingVoucherOpt.get();
        voucher.setStatus(status);
        voucherRepository.save(voucher);
        return new BaseResultDTO(ResultNotify.successUpdate, true, voucher);
    }

    @Override
    public BaseResultDTO deleteVoucher(Long id) {
        voucherRepository.deleteById(id);
        return new BaseResultDTO(ResultNotify.successDelete, true, null);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResultDTO checkVoucher(String code, Long companyId, BigDecimal orderAmount) {
        Optional<Voucher> voucherOpt = voucherRepository.findByCodeAndCompanyId(code, companyId);
        if (voucherOpt.isEmpty()) {
            return new BaseResultDTO(ResultNotify.notFound, VoucherConstant.Message.NOT_FOUND, false);
        }
        Voucher voucher = voucherOpt.get();

        String invalidReason = validate(voucher, orderAmount);
        if (invalidReason != null) {
            return new BaseResultDTO(ResultNotify.error, invalidReason, false);
        }

        BigDecimal discountAmount = calculateDiscount(voucher, orderAmount);
        VoucherApplyResult result = new VoucherApplyResult(voucher.getId(), voucher.getCode(), voucher.getDiscountType(), discountAmount);
        return new BaseResultDTO(ResultNotify.successGet, true, result);
    }

    @Override
    public BaseResultDTO applyVoucher(String code, Long companyId, BigDecimal orderAmount) {
        Optional<Voucher> voucherOpt = voucherRepository.findByCodeAndCompanyId(code, companyId);
        if (voucherOpt.isEmpty()) {
            return new BaseResultDTO(ResultNotify.notFound, VoucherConstant.Message.NOT_FOUND, false);
        }
        Voucher voucher = voucherOpt.get();

        String invalidReason = validate(voucher, orderAmount);
        if (invalidReason != null) {
            return new BaseResultDTO(ResultNotify.error, invalidReason, false);
        }

        BigDecimal discountAmount = calculateDiscount(voucher, orderAmount);

        voucher.setUsedCount((voucher.getUsedCount() == null ? 0 : voucher.getUsedCount()) + 1);
        voucherRepository.save(voucher);

        VoucherApplyResult result = new VoucherApplyResult(voucher.getId(), voucher.getCode(), voucher.getDiscountType(), discountAmount);
        return new BaseResultDTO(ResultNotify.successGet, true, result);
    }

    @Override
    public void releaseVoucher(Long voucherId) {
        if (voucherId == null) return;
        voucherRepository.findById(voucherId).ifPresent(voucher -> {
            int usedCount = voucher.getUsedCount() == null ? 0 : voucher.getUsedCount();
            voucher.setUsedCount(Math.max(0, usedCount - 1));
            voucherRepository.save(voucher);
        });
    }

    private String validate(Voucher voucher, BigDecimal orderAmount) {
        if (voucher.getStatus() == null || !voucher.getStatus().equals(VoucherConstant.Status.ACTIVE)) {
            return VoucherConstant.Message.INACTIVE;
        }
        ZonedDateTime now = ZonedDateTime.now();
        if (voucher.getStartDate() != null && now.isBefore(voucher.getStartDate())) {
            return VoucherConstant.Message.NOT_STARTED;
        }
        if (voucher.getEndDate() != null && now.isAfter(voucher.getEndDate())) {
            return VoucherConstant.Message.EXPIRED;
        }
        if (voucher.getUsageLimit() != null) {
            int usedCount = voucher.getUsedCount() == null ? 0 : voucher.getUsedCount();
            if (usedCount >= voucher.getUsageLimit()) {
                return VoucherConstant.Message.USAGE_LIMIT_REACHED;
            }
        }
        if (voucher.getMinOrderAmount() != null && orderAmount != null
                && orderAmount.compareTo(voucher.getMinOrderAmount()) < 0) {
            return VoucherConstant.Message.MIN_ORDER_AMOUNT_NOT_MET;
        }
        return null;
    }

    private BigDecimal calculateDiscount(Voucher voucher, BigDecimal orderAmount) {
        BigDecimal discount;
        if (VoucherConstant.DiscountType.PERCENT.equals(voucher.getDiscountType())) {
            discount = orderAmount.multiply(voucher.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discount = voucher.getDiscountValue();
        }
        if (voucher.getMaxDiscountAmount() != null && discount.compareTo(voucher.getMaxDiscountAmount()) > 0) {
            discount = voucher.getMaxDiscountAmount();
        }
        if (discount.compareTo(orderAmount) > 0) {
            discount = orderAmount;
        }
        return discount;
    }
}
