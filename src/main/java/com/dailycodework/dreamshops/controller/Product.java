package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.product.ProductInfo;
import com.dailycodework.dreamshops.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Product {
    private final ProductService productService;

    @GetMapping("/product/get-with-paging")
    public ResponseEntity<BaseResultDTO> getProductWithPaging(
            @org.springdoc.api.annotations.ParameterObject Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal price
    ){
        BaseResultDTO result = productService.getProductWithPaging(
                pageable,
                keyword,
                price
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/product/find-by-id/{id}")
    public ResponseEntity<BaseResultDTO> findById(@PathVariable(value = "id") Long id){
        BaseResultDTO result = productService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/product/create")
    public ResponseEntity<BaseResultDTO> createProduct(@RequestBody ProductInfo productReq) {
        BaseResultDTO result = productService.createProduct(productReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/product/update")
    public ResponseEntity<BaseResultDTO> updateProduct(@RequestBody ProductInfo productReq){
        BaseResultDTO result = productService.updateProduct(productReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<BaseResultDTO> deleteProduct(@PathVariable(value = "id") Long id){
        BaseResultDTO result = productService.deleteProduct(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
