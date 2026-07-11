package com.dailycodework.dreamshops.repository.order;

import com.dailycodework.dreamshops.payload.dto.order.OrderInfo;
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
public class IOrderRepositoryImpl implements OrderRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Page<OrderInfo> getOrderWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            String orderCode,
            Integer status,
            Integer companyId
    ){
        Map<String, Object> params = new HashMap<>();
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");

        if (keyword != null && !keyword.isEmpty()) {
            where.append(" AND o.code LIKE :keyword ");
            params.put("keyword", "%" + keyword + "%");
        }
        if (orderCode != null && !orderCode.isEmpty()) {
            where.append(" AND o.code LIKE :orderCode ");
            params.put("orderCode", "%" + orderCode + "%");
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            where.append(" AND o.order_date >= CAST(:fromDate AS DATE) ");
            params.put("fromDate", fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            where.append(" AND o.order_date < DATEADD(day, 1, CAST(:toDate AS DATE)) ");
            params.put("toDate", toDate);
        }
        if (status != null) {
            where.append(" AND o.status = :status ");
            params.put("status", status);
        }
        if (companyId != null) {
            where.append(" AND o.company_id = :companyId ");
            params.put("companyId", companyId);
        }

        Query countQuery = entityManager.createNativeQuery(
                "SELECT COUNT(DISTINCT o.id) FROM orders o" + where
        );
        params.forEach(countQuery::setParameter);
        long totalItem = ((Number) countQuery.getSingleResult()).longValue();

        Query query = entityManager.createNativeQuery(
                "SELECT " +
                        "o.id, " +
                        "o.code, " +
                        "o.customer_id customerId, " +
                        "o.order_date orderDate, " +
                        "o.description, " +
                        "o.discount_amount discountAmount, " +
                        "o.vat_rate vatRate, " +
                        "o.vat_amount vatAmount, " +
                        "o.total_amount totalAmount, " +
                        "o.company_id companyId, " +
                        "o.status, " +
                        "o.extra, " +
                        "o.voucher_id voucherId, " +
                        "o.voucher_code voucherCode " +
                        "FROM orders o " +
                        "LEFT JOIN order_product op ON o.id = op.order_id " +
                        where +
                        " ORDER BY o.id DESC",
                "OrderResponse"
        );
        Common.setParamsWithPageable(query, params, pageable, totalItem);
        List<OrderInfo> orderResponse = query.getResultList();
        return new PageImpl<>(orderResponse, pageable, totalItem);
    }
}
