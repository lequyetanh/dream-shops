package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.product.ProductInfo;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface IProductService {
    public BaseResultDTO getProductWithPaging(
            Pageable pageable,
            String sort,
            Integer companyId,
            String keyword
    );
    public BaseResultDTO findById(Long id);
    public BaseResultDTO createProduct(ProductInfo productReq);
    public BaseResultDTO updateProduct(ProductInfo productReq);
    public BaseResultDTO deleteProduct(Long id);
}
