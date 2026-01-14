package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.product.ProductInfo;
import com.dailycodework.dreamshops.dto.product.ProductResponse;
import com.dailycodework.dreamshops.entity.Product;
import com.dailycodework.dreamshops.repository.product.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    public final IProductRepository productRepository;

    @Override
    public BaseResultDTO getProductWithPaging(
            Pageable pageable,
            String sort,
            Integer companyId,
            String keyword
    ){
        List<ProductResponse> productResponses = new ArrayList<>();
        Page<ProductResponse> productList = productRepository.getWithPaging(
                pageable,
                sort,
                companyId,
                keyword
        );
        productResponses = productList.getContent();
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                productResponses
        );
    };

    @Override
    public BaseResultDTO findById(Long id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                product.get()
        );
    };

    @Override
    public BaseResultDTO createProduct(ProductInfo productReq){
        Product product = new Product();
        BeanUtils.copyProperties(productReq,product);
        productRepository.save(product);
        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                product
        );
    };

    @Override
    public BaseResultDTO updateProduct(ProductInfo productReq){
        Product product = new Product();
        BeanUtils.copyProperties(productReq,product);
        productRepository.save(product);
        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                product
        );
    };

    @Override
    public BaseResultDTO deleteProduct(Long id){
        productRepository.deleteById(id);
        return new BaseResultDTO(
                ResultNotify.successDelete,
                true,
                null
        );
    };
}
