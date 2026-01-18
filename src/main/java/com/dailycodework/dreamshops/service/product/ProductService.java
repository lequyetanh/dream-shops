package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.product.ProductInfo;
import com.dailycodework.dreamshops.dto.product.ProductResponse;
import com.dailycodework.dreamshops.entity.Product;
import com.dailycodework.dreamshops.repository.product.IProductRepository;
import com.dailycodework.dreamshops.service.RedisManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tools.jackson.core.JsonParser;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.dailycodework.dreamshops.constant.RedisConstant.PRODUCT_LIST;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    public final IProductRepository productRepository;
    public final RedisManagementService redisManagementService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BaseResultDTO getProductWithPaging(
            Pageable pageable,
            String sort,
            Integer companyId,
            String keyword
    ){
        List<ProductResponse> productResponses = new ArrayList<>();
        if(redisManagementService.getValue(PRODUCT_LIST) != null){
            Object value = redisManagementService.getValue(PRODUCT_LIST);

            if (value != null) {
                String json = (String) value;
                    productResponses =
                        objectMapper.readValue(
                                json,
                                new TypeReference<List<ProductResponse>>() {}
                        );
            }
        }else{
            Page<ProductResponse> productList = productRepository.getWithPaging(
                    pageable,
                    sort,
                    companyId,
                    keyword
            );
            productResponses = productList.getContent();
            redisManagementService.setValueWithTimeUnit(PRODUCT_LIST, objectMapper.writeValueAsString(productResponses), 300, TimeUnit.MINUTES);
        }
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
