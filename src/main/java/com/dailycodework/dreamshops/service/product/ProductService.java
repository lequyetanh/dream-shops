package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.product.ProductInfo;
import com.dailycodework.dreamshops.repository.product.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    public final IProductRepository productRepository;

    @Override
    public BaseResultDTO getProductWidthPaging(
            Pageable pageable,
            String keyword,
            BigDecimal price
    ){
        return null;
    };

    @Override
    public BaseResultDTO findById(Long id){
        return null;
    };

    @Override
    public BaseResultDTO createProduct(ProductInfo productReq){
        return null;
    };

    @Override
    public BaseResultDTO updateProduct(ProductInfo productReq){
        return null;
    };

    @Override
    public BaseResultDTO deleteProduct(Long id){
        return null;
    };
}
