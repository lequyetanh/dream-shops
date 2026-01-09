package com.dailycodework.dreamshops.mapper.category;

import com.dailycodework.dreamshops.dto.category.CategoryInfo;
import com.dailycodework.dreamshops.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryInfo categoryDTO);
}
