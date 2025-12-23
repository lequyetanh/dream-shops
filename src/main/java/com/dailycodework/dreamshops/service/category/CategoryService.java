package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.category.CreateCategoryReq;
import com.dailycodework.dreamshops.dto.category.GetCategoryWithPaging;
import com.dailycodework.dreamshops.entity.Category;
import com.dailycodework.dreamshops.repository.category.ICategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public BaseResultDTO createCategory(CreateCategoryReq categoryReq) {
        Category category = new Category();
        category.setName(categoryReq.getName());
        category.setDescription(categoryReq.getDescription());
        categoryRepository.save(category);
        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                category
        );
    }

    @Override
    public BaseResultDTO updateCategory(CreateCategoryReq categoryReq) {
        return null;
    }

    @Override
    public BaseResultDTO deleteCategory(CreateCategoryReq categoryReq) {
        return null;
    }

    @Override
    public BaseResultDTO getCategoryWithPaging(GetCategoryWithPaging categoryReq) {
        List<Category> listCategory = categoryRepository.findAll();
        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                listCategory
        );
    }
}
