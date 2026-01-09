package com.dailycodework.dreamshops.repository.product;

import com.dailycodework.dreamshops.dto.product.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductResponse> getWithPaging(
            Pageable pageable,
            String sort,
            Integer companyId,
            String keyword
    );
}
