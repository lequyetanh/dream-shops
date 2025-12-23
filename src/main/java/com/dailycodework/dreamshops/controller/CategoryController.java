package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.category.CreateCategoryReq;
import com.dailycodework.dreamshops.dto.category.GetCategoryWithPaging;
import com.dailycodework.dreamshops.service.category.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(
            CategoryService categoryService
    ) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category/get-with-paging")
    public ResponseEntity<BaseResultDTO> getCategoryWithPaging(@RequestBody GetCategoryWithPaging categoryReq){
        return ResponseEntity.ok().body(categoryService.getCategoryWithPaging(categoryReq));
    }

    @PostMapping("/category/create")
    public ResponseEntity<BaseResultDTO> createCategory(@RequestBody CreateCategoryReq categoryReq) {
        return ResponseEntity.ok().body(categoryService.createCategory(categoryReq));
    }

}
