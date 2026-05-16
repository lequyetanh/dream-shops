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

import java.util.ArrayList;
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
    ){
        List<ProductResponse> productResponses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" from product p ");
        if(keyword != null && !keyword.isEmpty()){
            sql.append(" where p.company_id = :companyId and ( p.product_name like :keyword or p.product_code like :keyword ) ");
            params.put("keyword", "%" + keyword + "%");
        } else {
            sql.append(" where p.company_id = :companyId ");
        }
        params.put("companyId", companyId);
        if(sort != null && !sort.isEmpty()){
            sql.append(" order by p.").append(sort);
        } else {
            sql.append(" order by p.id desc ");
        }

        Query query = entityManager.createNativeQuery(
                "select " +
                    "p.id, p.name, p.description, p.barcode, " +
                    "p.image, p.in_price inPrice, p.out_price outPrice, p.company_id companyId, " +
                    "p.stock_quantity stockQuantity " +
                    sql,
                "ProductResponse"
        );

        Common.setParamsWithPageable(query, params, pageable, 0);
        productResponses = query.getResultList();
        return new PageImpl<>(productResponses, pageable, 0);
    }
}
