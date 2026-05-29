package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.entity.Category;
import com.dailycodework.dreamshops.entity.Product;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.product.ProductInfo;
import com.dailycodework.dreamshops.payload.dto.product.ProductResponse;
import com.dailycodework.dreamshops.repository.category.ICategoryRepository;
import com.dailycodework.dreamshops.repository.product.IProductRepository;
import com.dailycodework.dreamshops.service.RedisManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
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
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }
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
        return new BaseResultDTO(ResultNotify.successUpdate, true, product);
    }

    @Override
    public BaseResultDTO deleteProduct(Long id) {
        productRepository.deleteById(id);
        return new BaseResultDTO(ResultNotify.successDelete, true, null);
    }
}
