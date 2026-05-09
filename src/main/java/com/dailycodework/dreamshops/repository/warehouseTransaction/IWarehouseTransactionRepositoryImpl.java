package com.dailycodework.dreamshops.repository.warehouseTransaction;

import com.dailycodework.dreamshops.dto.order.OrderInfo;
import com.dailycodework.dreamshops.dto.warehouseTransaction.WarehouseTransactionList;
import com.dailycodework.dreamshops.util.Common;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                        "wt.id, " +
                        "wt.company_id companyId, " +
                        "wt.no, " +
                        "wt.date, " +
                        "wt.description, " +
                        "wt.amount, " +
                        "wt.vat_rate vatRate, " +
                        "wt.vat_amount vatAmount, " +
                        "wt.total_amount totalAmount, " +
                        sql,
                Tuple.class
        );
        Common.setParamsWithPageable(query, params, pageable, 0);
        List<Tuple> tuples = query.getResultList();
        warehouseTransactionLists = tuples.stream().map(tuple -> {
            WarehouseTransactionList warehouseTransactionList = new WarehouseTransactionList();
            warehouseTransactionList.setId(tuple.get("id", Long.class));
            warehouseTransactionList.setCompanyId(tuple.get("companyId", Integer.class));
            warehouseTransactionList.setNo(tuple.get("no", String.class));
            warehouseTransactionList.setDate(tuple.get("date", ZonedDateTime.class));
            warehouseTransactionList.setDescription(tuple.get("description", String.class));
            warehouseTransactionList.setAmount(tuple.get("amount", BigDecimal.class));
            warehouseTransactionList.setVatRate(tuple.get("vatRate", Integer.class));
            warehouseTransactionList.setVatAmount(tuple.get("vatAmount", BigDecimal.class));
            warehouseTransactionList.setTotalAmount(tuple.get("totalAmount", BigDecimal.class));
            return warehouseTransactionList;
        }).collect(Collectors.toList());
        return new PageImpl<>(warehouseTransactionLists, pageable, totalItem);
    }
}
