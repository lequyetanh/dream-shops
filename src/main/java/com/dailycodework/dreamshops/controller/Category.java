package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.category.CategroyInfo;
import com.dailycodework.dreamshops.service.category.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Category {
    private final CategoryService categoryService;
    public Category(
            CategoryService categoryService
    ) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category/get-with-paging")
    public ResponseEntity<BaseResultDTO> getCategoryWithPaging(
            Pageable pageable,
            @RequestParam(required = false) String keyword
            ){
        BaseResultDTO result = categoryService.getCategoryWithPaging(
                pageable,
                keyword
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/category/find-by-id/{id}")
    public ResponseEntity<BaseResultDTO> findById(@PathVariable(value = "id") Long id){
        BaseResultDTO result = categoryService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/category/create")
    public ResponseEntity<BaseResultDTO> createCategory(@RequestBody CategroyInfo categoryReq) {
        BaseResultDTO result = categoryService.createCategory(categoryReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/category/update")
    public ResponseEntity<BaseResultDTO> updateCategory(@RequestBody CategroyInfo categoryReq){
        BaseResultDTO result = categoryService.updateCategory(categoryReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/category/delete/{id}")
    public ResponseEntity<BaseResultDTO> deleteCategory(@PathVariable(value = "id") Long id){
        BaseResultDTO result = categoryService.deleteCategory(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
