package com.dailycodework.dreamshops.mapper.category;

import com.dailycodework.dreamshops.dto.category.CategroyInfo;
import com.dailycodework.dreamshops.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategroyInfo categoryDTO);
}
