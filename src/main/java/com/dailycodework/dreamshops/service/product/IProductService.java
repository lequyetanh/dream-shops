package com.dailycodework.dreamshops.service.product;

import com.dailycodework.dreamshops.entity.Order;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.product.ProductInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    void updateStockQuantity(List<Order> orders);
    BaseResultDTO getProductWithPaging(Pageable pageable, String sort, Integer companyId, String keyword);
    BaseResultDTO findById(Long id);
    BaseResultDTO findByBarcode(String barcode);
    BaseResultDTO getLowStockProducts(Long companyId, Integer threshold);
    BaseResultDTO createProduct(ProductInfo productReq);
    BaseResultDTO updateProduct(ProductInfo productReq);
    BaseResultDTO deleteProduct(Long id);
}
