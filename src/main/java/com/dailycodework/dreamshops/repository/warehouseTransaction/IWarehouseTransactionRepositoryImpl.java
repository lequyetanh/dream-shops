package com.dailycodework.dreamshops.repository.warehouseTransaction;

import com.dailycodework.dreamshops.dto.order.OrderInfo;
import com.dailycodework.dreamshops.dto.warehouseTransaction.WarehouseTransactionList;
import com.dailycodework.dreamshops.util.Common;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class IWarehouseTransactionRepositoryImpl implements  IWarehouseTransactionRepository {
    private final EntityManager entityManager;

    @Override
    public Page<WarehouseTransactionList> getWarehouseTransactionWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            Integer companyId
    ){
        Integer totalItem = 0;
        StringBuilder countSql = new StringBuilder();
        countSql.append("select count(*) from warehouse_transaction wt");
        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        totalItem = (Integer) countQuery.getSingleResult();

        List<WarehouseTransactionList> warehouseTransactionLists = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" from warehouse_transaction wt left join warehouse_transaction_detail op on o.id = op.order_id ");
        if(keyword != null && !keyword.isEmpty()){
            sql.append(" where (wt.code like :keyword) ");
            params.put("keyword", "%" + keyword + "%");
        }
        if(companyId != null){
            sql.append(" and wt.company_id = :companyId ");
            params.put("companyId", companyId);
        }

        Query query = entityManager.createNativeQuery(
                "select " +
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
                        "o.status status, " +
                        "o.extra " +
                        sql,
                "OrderResponse"
        );
        Common.setParamsWithPageable(query, params, pageable, 0);
        orderResponse = query.getResultList();
        return new PageImpl<>(orderResponse, pageable, totalItem);
    }
}
