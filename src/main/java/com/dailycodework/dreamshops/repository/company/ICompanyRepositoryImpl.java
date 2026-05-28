package com.dailycodework.dreamshops.repository.company;

import com.dailycodework.dreamshops.entity.Company;
import com.dailycodework.dreamshops.util.Common;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ICompanyRepositoryImpl implements CompanyRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Page<Company> getWithPaging(Pageable pageable, String keyword) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");

        if (keyword != null && !keyword.isEmpty()) {
            where.append(" AND (c.name LIKE :keyword OR c.phone LIKE :keyword OR c.tax_code LIKE :keyword) ");
            params.put("keyword", "%" + keyword + "%");
        }

        Query countQuery = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM company c" + where
        );
        params.forEach(countQuery::setParameter);
        long total = ((Number) countQuery.getSingleResult()).longValue();

        Query query = entityManager.createNativeQuery(
                "SELECT * FROM company c" + where + " ORDER BY c.id DESC",
                Company.class
        );
        Common.setParamsWithPageable(query, params, pageable, total);

        List<Company> companies = query.getResultList();
        return new PageImpl<>(companies, pageable, total);
    }
}
