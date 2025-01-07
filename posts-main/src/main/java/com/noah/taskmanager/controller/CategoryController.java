package com.noah.taskmanager.controller;

import com.noah.taskmanager.dto.CategoryCreateDTO;
import com.noah.taskmanager.dto.CategoryReadDTO;
import com.noah.taskmanager.dto.CategoryUpdateDTO;
import com.noah.taskmanager.mapper.CategoryMapper;
import com.noah.taskmanager.model.Category;
import com.noah.taskmanager.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public List<CategoryReadDTO> getAllCategories() {
        return categoryService.getAllCategories()
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryReadDTO> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryMapper.toDTO(category));
    }

    @PostMapping
    public ResponseEntity<CategoryReadDTO> createCategory(@Valid @RequestBody CategoryCreateDTO categoryCreateDTO) {
        Category category = categoryMapper.toEntity(categoryCreateDTO);
        Category createdCategory = categoryService.createCategory(category);
        return ResponseEntity.ok(categoryMapper.toDTO(createdCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryReadDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        // Retrieve the existing category
        Category existingCategory = categoryService.getCategoryById(id);

        // Update the existing category using the mapper
        categoryMapper.updateEntityFromDTO(categoryUpdateDTO, existingCategory);

        // Save the updated category
        Category updatedCategory = categoryService.updateCategory(existingCategory);

        // Return the updated DTO
        return ResponseEntity.ok(categoryMapper.toDTO(updatedCategory));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
