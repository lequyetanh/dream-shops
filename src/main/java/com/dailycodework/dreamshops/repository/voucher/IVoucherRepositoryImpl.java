package com.dailycodework.dreamshops.repository.voucher;

import com.dailycodework.dreamshops.payload.dto.voucher.VoucherInfo;
import com.dailycodework.dreamshops.util.Common;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class IVoucherRepositoryImpl implements VoucherRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Page<VoucherInfo> getVoucherWithPaging(
            Pageable pageable,
            String keyword,
            String code,
            Integer status,
            Long companyId
    ) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");

        if (keyword != null && !keyword.isEmpty()) {
            where.append(" AND (v.code LIKE :keyword OR v.name LIKE :keyword) ");
            params.put("keyword", "%" + keyword + "%");
        }
        if (code != null && !code.isEmpty()) {
            where.append(" AND v.code LIKE :code ");
            params.put("code", "%" + code + "%");
        }
        if (status != null) {
            where.append(" AND v.status = :status ");
            params.put("status", status);
        }
        if (companyId != null) {
            where.append(" AND v.company_id = :companyId ");
            params.put("companyId", companyId);
        }

        Query countQuery = entityManager.createNativeQuery(
                "SELECT COUNT(v.id) FROM voucher v" + where
        );
        params.forEach(countQuery::setParameter);
        long totalItem = ((Number) countQuery.getSingleResult()).longValue();

        Query query = entityManager.createNativeQuery(
                "SELECT " +
                        "v.id, " +
                        "v.code, " +
                        "v.name, " +
                        "v.description, " +
                        "v.discount_type discountType, " +
                        "v.discount_value discountValue, " +
                        "v.min_order_amount minOrderAmount, " +
                        "v.max_discount_amount maxDiscountAmount, " +
                        "v.start_date startDate, " +
                        "v.end_date endDate, " +
                        "v.usage_limit usageLimit, " +
                        "v.used_count usedCount, " +
                        "v.status, " +
                        "v.company_id companyId, " +
                        "v.extra " +
                        "FROM voucher v" +
                        where +
                        " ORDER BY v.id DESC",
                "VoucherResponse"
        );
        Common.setParamsWithPageable(query, params, pageable, totalItem);
        List<VoucherInfo> voucherList = query.getResultList();
        return new PageImpl<>(voucherList, pageable, totalItem);
    }
}
