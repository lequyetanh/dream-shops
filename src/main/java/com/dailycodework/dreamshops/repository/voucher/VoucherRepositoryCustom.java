package com.dailycodework.dreamshops.repository.voucher;

import com.dailycodework.dreamshops.payload.dto.voucher.VoucherInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoucherRepositoryCustom {
    Page<VoucherInfo> getVoucherWithPaging(
            Pageable pageable,
            String keyword,
            String code,
            Integer status,
            Long companyId
    );
}
