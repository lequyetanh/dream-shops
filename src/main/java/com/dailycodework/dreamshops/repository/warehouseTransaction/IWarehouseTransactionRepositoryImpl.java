package com.dailycodework.dreamshops.repository.warehouseTransaction;

import com.dailycodework.dreamshops.payload.dto.warehouseTransaction.WarehouseTransactionList;
import com.dailycodework.dreamshops.util.Common;
import com.dailycodework.dreamshops.util.TupleMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
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
public class IWarehouseTransactionRepositoryImpl implements WarehouseTransactionRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Page<WarehouseTransactionList> getWarehouseTransactionWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            Integer companyId
    ) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");

        if (keyword != null && !keyword.isEmpty()) {
            where.append(" AND wt.code LIKE :keyword ");
            params.put("keyword", "%" + keyword + "%");
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            where.append(" AND wt.date >= CAST(:fromDate AS DATE) ");
            params.put("fromDate", fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            where.append(" AND wt.date < DATEADD(day, 1, CAST(:toDate AS DATE)) ");
            params.put("toDate", toDate);
        }
        if (companyId != null) {
            where.append(" AND wt.company_id = :companyId ");
            params.put("companyId", companyId);
        }

        Query countQuery = entityManager.createNativeQuery(
                "SELECT COUNT(DISTINCT wt.id) FROM warehouse_transaction wt " +
                "LEFT JOIN warehouse_transaction_detail wtd ON wt.id = wtd.warehouse_transaction_id " +
                where
        );
        params.forEach(countQuery::setParameter);
        long totalItem = ((Number) countQuery.getSingleResult()).longValue();

        Query query = entityManager.createNativeQuery(
                "SELECT " +
                        "wt.id, " +
                        "wt.company_id companyId, " +
                        "wt.no, " +
                        "wt.date, " +
                        "wt.description, " +
                        "wt.amount, " +
                        "wt.vat_rate vatRate, " +
                        "wt.vat_amount vatAmount, " +
                        "wt.total_amount totalAmount " +
                "FROM warehouse_transaction wt " +
                "LEFT JOIN warehouse_transaction_detail wtd ON wt.id = wtd.warehouse_transaction_id " +
                where +
                " ORDER BY wt.id DESC",
                Tuple.class
        );
        Common.setParamsWithPageable(query, params, pageable, totalItem);

        List<WarehouseTransactionList> results = TupleMapper.mapList(query, WarehouseTransactionList.class);
        return new PageImpl<>(results, pageable, totalItem);
    }
}
