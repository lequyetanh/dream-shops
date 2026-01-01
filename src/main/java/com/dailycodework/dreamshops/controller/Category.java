package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.category.CategroyInfo;
import com.dailycodework.dreamshops.dto.category.GetCategoryWithPagingRes;
import com.dailycodework.dreamshops.service.category.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class Category {
    private final CategoryService categoryService;
    public Category(
            CategoryService categoryService
    ) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category/get-with-paging")
    public ResponseEntity<BaseResultDTO> getCategoryWithPaging(@RequestBody GetCategoryWithPagingRes categoryReq){
        return ResponseEntity.ok().body(categoryService.getCategoryWithPaging(categoryReq));
    }

    @PostMapping("/category/create")
    public ResponseEntity<BaseResultDTO> createCategory(@RequestBody CategroyInfo categoryReq) {
        return ResponseEntity.ok().body(categoryService.createCategory(categoryReq));
    }

}
