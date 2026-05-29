package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.category.CategoryInfo;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    BaseResultDTO getCategoryWithPaging(Pageable pageable, Long companyId, String keyword);
    BaseResultDTO findById(Long id);
    BaseResultDTO createCategory(CategoryInfo categoryReq);
    BaseResultDTO updateCategory(CategoryInfo categoryReq);
    BaseResultDTO deleteCategory(Long id);
}
