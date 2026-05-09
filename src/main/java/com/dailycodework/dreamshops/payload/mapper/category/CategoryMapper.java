package com.dailycodework.dreamshops.payload.mapper.category;

import com.dailycodework.dreamshops.payload.dto.category.CategoryInfo;
import com.dailycodework.dreamshops.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryInfo categoryDTO);
}
