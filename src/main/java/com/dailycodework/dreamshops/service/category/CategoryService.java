package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.dto.category.CreateCategoryReq;
import com.dailycodework.dreamshops.entity.Category;
import com.dailycodework.dreamshops.repository.category.ICategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void createCategory(CreateCategoryReq categoryReq) {
        Category category = new Category();
        category.setName(categoryReq.getName());
        category.setDescription(categoryReq.getDescription());
        categoryRepository.save(category);
    }
}
