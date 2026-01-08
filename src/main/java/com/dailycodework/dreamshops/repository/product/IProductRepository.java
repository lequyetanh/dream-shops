package com.dailycodework.dreamshops.repository.product;

import com.dailycodework.dreamshops.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductRepository extends CrudRepository<Product,Long> {
    Optional<Product> findById(Long id);
}
