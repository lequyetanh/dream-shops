package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.product.ProductInfo;
import com.dailycodework.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Product {
    private final IProductService productService;

    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    @GetMapping("/product/get-with-paging")
    public ResponseEntity<BaseResultDTO> getProductWithPaging(
            Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) Integer companyId,
            @RequestParam(required = false) String sort
    ){
        BaseResultDTO result = productService.getProductWithPaging(
                pageable,
                sort,
                companyId,
                keyword
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    @GetMapping("/product/find-by-id/{id}")
    public ResponseEntity<BaseResultDTO> findById(@PathVariable(value = "id") Long id){
        BaseResultDTO result = productService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    @PostMapping("/product/create")
    public ResponseEntity<BaseResultDTO> createProduct(@RequestBody ProductInfo productReq) {
        BaseResultDTO result = productService.createProduct(productReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    @PostMapping("/product/update")
    public ResponseEntity<BaseResultDTO> updateProduct(@RequestBody ProductInfo productReq){
        BaseResultDTO result = productService.updateProduct(productReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<BaseResultDTO> deleteProduct(@PathVariable(value = "id") Long id){
        BaseResultDTO result = productService.deleteProduct(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
