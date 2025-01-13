package com.noah.taskmanager.dto;

public class CategoryReadDTO {
    private Long categoryId;
    private String categoryName;

    // Default Constructor
    public CategoryReadDTO() {
        // Default no-argument constructor
    }

    // Constructor
    public CategoryReadDTO(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
