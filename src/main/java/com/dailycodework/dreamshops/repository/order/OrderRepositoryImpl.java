package com.dailycodework.dreamshops.repository.order;

import com.dailycodework.dreamshops.dto.order.OrderInfo;
import com.dailycodework.dreamshops.util.Common;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    Page<OrderInfo> getOrdersWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            String orderCode,
            Integer status
    ){
        List<OrderInfo> orderResponse = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" from orders o");
        if(keyword != null && !keyword.isEmpty()){
            sql.append(" where (o.customer_name like :keyword or o.customer_phone like :keyword or o.customer_email like :keyword)");
            params.put("keyword", "%" + keyword + "%");
        }
        Query query = entityManager.createNativeQuery(
                "select " +
                        "o.id, " +
                        "o.code, " +
                        "o.customer_id customerId, " +
                        "o.order_date orderDate, " +
                        "o.description, " +
                        "o.discount_amount discountAmount, " +
                        "o.vat_rate vatRate " +
                        "o.vat_amount vatAmount " +
                        "o.total_amount totalAmount " +
                        "o.company_id companyId " +
                        "o.extra " +
                        sql,
                "OrderInfo"
        );
        Common.setParamsWithPageable(query, params, pageable, 0);
        orderResponse = query.getResultList();
        return new PageImpl<>(orderResponse, pageable, 0);
    }
}
