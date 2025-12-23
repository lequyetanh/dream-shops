package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.category.CreateCategoryReq;
import com.dailycodework.dreamshops.dto.category.GetCategoryWithPaging;

public interface ICategoryService {
    public BaseResultDTO createCategory(CreateCategoryReq categoryReq);
    public BaseResultDTO updateCategory(CreateCategoryReq categoryReq);
    public BaseResultDTO deleteCategory(CreateCategoryReq categoryReq);
    public BaseResultDTO getCategoryWithPaging(GetCategoryWithPaging categoryReq);
}
