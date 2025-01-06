package dev.danvega.danson.repository;

import dev.danvega.danson.model.Category;
import dev.danvega.danson.model.TaskCategory;
import dev.danvega.danson.model.TaskCategoryId;
import dev.danvega.danson.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, TaskCategoryId> {

    // Find all TaskCategory relationships for a specific task
    List<TaskCategory> findByTask_Taskid(Long taskId);

    // Find all TaskCategory relationships for a specific category
    List<TaskCategory> findByCategory_CategoryId(Long categoryId);

    // Find all tasks associated with a specific category
    @Query("SELECT tc.task FROM TaskCategory tc WHERE tc.category.categoryId = :categoryId")
    List<TaskEntity> findTasksByCategoryId(Long categoryId);

    // Find all categories associated with a specific task
    @Query("SELECT tc.category FROM TaskCategory tc WHERE tc.task.taskid = :taskId")
    List<Category> findCategoriesByTaskId(Long taskId);
}
