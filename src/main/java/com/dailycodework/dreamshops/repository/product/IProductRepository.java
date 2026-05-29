package com.dailycodework.dreamshops.repository.product;

import com.dailycodework.dreamshops.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends CrudRepository<Product, Long>, ProductRepositoryCustom {
    Optional<Product> findByBarcode(String barcode);
    List<Product> findByCompanyIdAndStockQuantityLessThanEqual(Long companyId, Integer stockQuantity);
}
