package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.category.CategoryInfo;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    public BaseResultDTO getCategoryWithPaging(
            Pageable pageable,
            String keyword
    );
    public BaseResultDTO findById(Long id);
    public BaseResultDTO createCategory(CategoryInfo categoryReq);
    public BaseResultDTO updateCategory(CategoryInfo categoryReq);
    public BaseResultDTO deleteCategory(Long id);
}
