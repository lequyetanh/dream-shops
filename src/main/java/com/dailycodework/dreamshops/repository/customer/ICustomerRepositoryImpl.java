package com.dailycodework.dreamshops.repository.customer;

import com.dailycodework.dreamshops.payload.dto.customer.CustomerInfo;
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
public class ICustomerRepositoryImpl implements CustomerRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Page<CustomerInfo> getCustomerWithPaging(Pageable pageable, String keyword, Long companyId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder where = new StringBuilder(" where 1=1 ");

        if (companyId != null) {
            where.append(" and c.company_id = :companyId ");
            params.put("companyId", companyId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            where.append(" and (c.name like :keyword or c.code like :keyword or c.phone like :keyword) ");
            params.put("keyword", "%" + keyword + "%");
        }

        Query countQuery = entityManager.createNativeQuery(
                "select count(*) from customer c" + where
        );
        params.forEach(countQuery::setParameter);
        int total = ((Number) countQuery.getSingleResult()).intValue();

        Query query = entityManager.createNativeQuery(
                "select c.id, c.name, c.code, c.address, c.email, c.phone, c.type, c.company_id companyId " +
                "from customer c" + where + " order by c.id desc",
                "CustomerResponse"
        );
        Common.setParamsWithPageable(query, params, pageable, 0);

        List<CustomerInfo> result = query.getResultList();
        return new PageImpl<>(result, pageable, total);
    }
}
