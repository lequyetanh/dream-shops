package com.dailycodework.dreamshops.payload.dto.category;

import lombok.*;

@Data
public class CategoryInfo {
    private Long id;
    private String name;
    private String description;
    private Long companyId;
}