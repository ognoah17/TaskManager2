package com.noah.taskmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories", schema = "public")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @NaturalId
    @NotNull(message = "Category name cannot be null.")
    @Size(min = 1, max = 255, message = "Category name must be between 1 and 255 characters.")
    @Column(nullable = false, unique = true, name = "category_name")
    private String categoryName;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<TaskEntity> tasks = new HashSet<>();

    // Default Constructor
    public Category() {
        // Default constructor for JPA
    }

    // Constructor for Required Fields
    public Category(String categoryName) {
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

    public Set<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return STR."Category{categoryId=\{categoryId}, categoryName='\{categoryName}'}";
    }
}
