package com.dailycodework.dreamshops.repository.product;

import com.dailycodework.dreamshops.payload.dto.product.ProductResponse;
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
public class IProductRepositoryImpl implements ProductRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Page<ProductResponse> getWithPaging(
            Pageable pageable,
            String sort,
            Integer companyId,
            String keyword
    ) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");

        if (companyId != null) {
            where.append(" AND p.company_id = :companyId ");
            params.put("companyId", companyId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            where.append(" AND (p.product_name LIKE :keyword OR p.product_code LIKE :keyword) ");
            params.put("keyword", "%" + keyword + "%");
        }

        String orderBy = (sort != null && !sort.isEmpty())
                ? " ORDER BY p." + sort
                : " ORDER BY p.id DESC ";

        Query countQuery = entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM product p" + where
        );
        params.forEach(countQuery::setParameter);
        long total = ((Number) countQuery.getSingleResult()).longValue();

        Query query = entityManager.createNativeQuery(
                "SELECT p.id, p.name, p.description, p.barcode, " +
                "p.image, p.in_price inPrice, p.out_price outPrice, p.company_id companyId, " +
                "p.stock_quantity stockQuantity " +
                "FROM product p " + where + orderBy,
                "ProductResponse"
        );
        Common.setParamsWithPageable(query, params, pageable, total);

        List<ProductResponse> productResponses = query.getResultList();
        return new PageImpl<>(productResponses, pageable, total);
    }
}
