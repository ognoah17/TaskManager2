package com.noah.taskmanager.mapper;

import com.noah.taskmanager.dto.*;
import com.noah.taskmanager.model.*;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryCreateDTO dto) {
        Category category = new Category();
        category.setCategoryName(dto.getCategoryName());
        return category;
    }

    public CategoryReadDTO toDTO(Category category) {
        return new CategoryReadDTO(
                category.getCategoryId(),
                category.getCategoryName()
        );
    }

    public void updateEntityFromDTO(CategoryUpdateDTO dto, Category category) {
        if (dto.getCategoryName() != null) category.setCategoryName(dto.getCategoryName());
    }
}
