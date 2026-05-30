package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.entity.Category;
import com.dailycodework.dreamshops.entity.Order;
import com.dailycodework.dreamshops.entity.OrderProduct;
import com.dailycodework.dreamshops.entity.Product;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.product.ProductInfo;
import com.dailycodework.dreamshops.payload.dto.product.ProductResponse;
import com.dailycodework.dreamshops.repository.category.ICategoryRepository;
import com.dailycodework.dreamshops.repository.product.IProductRepository;
import com.dailycodework.dreamshops.service.RedisManagementService;
import com.dailycodework.dreamshops.util.Common;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private static final String PRODUCT_CACHE = "product:";

    public final IProductRepository productRepository;
    public final RedisManagementService redisManagementService;
    public final ICategoryRepository categoryRepository;

    @Override
    public BaseResultDTO getProductWithPaging(Pageable pageable, String sort, Integer companyId, String keyword) {
        Page<ProductResponse> productList = productRepository.getWithPaging(pageable, sort, companyId, keyword);
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                productList.getContent(),
                (int) productList.getTotalElements()
        );
    }

    @Override
    public BaseResultDTO findById(Long id) {
        String cacheKey = PRODUCT_CACHE + id;
        Object cached = redisManagementService.getValue(cacheKey);
        if (cached != null) {
            Product product = Common.fromJsonString(cached.toString(), Product.class);
            return new BaseResultDTO(ResultNotify.successGet, true, product);
        }
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }
        redisManagementService.setValue(cacheKey, Common.toJsonString(product.get()));
        return new BaseResultDTO(ResultNotify.successGet, true, product.get());
    }

    @Override
    public BaseResultDTO findByBarcode(String barcode) {
        Optional<Product> product = productRepository.findByBarcode(barcode);
        if (product.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm với barcode: " + barcode);
        }
        return new BaseResultDTO(ResultNotify.successGet, true, product.get());
    }

    @Override
    public BaseResultDTO getLowStockProducts(Long companyId, Integer threshold) {
        List<Product> products = productRepository.findByCompanyIdAndStockQuantityLessThanEqual(companyId, threshold);
        return new BaseResultDTO(ResultNotify.successGet, true, products, products.size());
    }

    @Override
    public BaseResultDTO createProduct(ProductInfo productReq) {
        Product product = new Product();
        BeanUtils.copyProperties(productReq, product, "categoryIds");
        List<Category> categories = categoryRepository.findAllById(productReq.getCategoryIds());
        product.getCategories().addAll(categories);
        productRepository.save(product);
        return new BaseResultDTO(ResultNotify.successCreate, true, product);
    }

    @Override
    public BaseResultDTO updateProduct(ProductInfo productReq) {
        Optional<Product> existing = productRepository.findById(productReq.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }
        Product product = existing.get();
        BeanUtils.copyProperties(productReq, product, "id", "categories", "categoryIds");
        product.getCategories().clear();
        List<Category> categories = categoryRepository.findAllById(productReq.getCategoryIds());
        product.getCategories().addAll(categories);
        productRepository.save(product);
        redisManagementService.deleteKey(PRODUCT_CACHE + productReq.getId());
        return new BaseResultDTO(ResultNotify.successUpdate, true, product);
    }

    @Override
    public BaseResultDTO deleteProduct(Long id) {
        productRepository.deleteById(id);
        redisManagementService.deleteKey(PRODUCT_CACHE + id);
        return new BaseResultDTO(ResultNotify.successDelete, true, null);
    }

    @Override
    public void updateStockQuantity(List<Order> orders) {
        Map<Long, BigDecimal> totalQtyByProductId = orders.stream()
                .flatMap(o -> o.getProducts().stream())
                .collect(Collectors.groupingBy(
                        OrderProduct::getProductId,
                        Collectors.reducing(BigDecimal.ZERO, OrderProduct::getQuantity, BigDecimal::add)
                ));

        totalQtyByProductId.forEach((productId, totalQty) ->
                productRepository.findById(productId).ifPresent(product -> {
                    product.setStockQuantity(product.getStockQuantity() - totalQty.intValue());
                    productRepository.save(product);
                    redisManagementService.deleteKey(PRODUCT_CACHE + productId);
                })
        );
    }
}
