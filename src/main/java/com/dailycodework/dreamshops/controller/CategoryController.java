package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.category.CreateCategoryReq;
import com.dailycodework.dreamshops.repository.category.ICategoryRepository;
import com.dailycodework.dreamshops.service.category.CategoryService;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/category/create")
    public void createCategory(@RequestBody CreateCategoryReq categoryReq) {
        this.categoryService.createCategory(categoryReq);
    }

}
