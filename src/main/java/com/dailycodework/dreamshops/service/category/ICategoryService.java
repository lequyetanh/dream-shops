package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.category.CreateCategoryReq;

public interface ICategoryService {
    public BaseResultDTO createCategory(CreateCategoryReq categoryReq);
}
