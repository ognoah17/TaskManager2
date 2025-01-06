package dev.danvega.danson.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "task_categories", schema = "public",
        indexes = {
                @Index(name = "idx_task_id", columnList = "task_id"),
                @Index(name = "idx_category_id", columnList = "category_id")
        }
)
@NamedQueries({
        @NamedQuery(
                name = "TaskCategory.findByTask",
                query = "SELECT tc FROM TaskCategory tc WHERE tc.task.taskid = :taskId"
        ),
        @NamedQuery(
                name = "TaskCategory.findByCategory",
                query = "SELECT tc FROM TaskCategory tc WHERE tc.category.categoryId = :categoryId"
        )
})
public class TaskCategory {

    @EmbeddedId
    private TaskCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("taskId")
    @JoinColumn(name = "task_id", nullable = false)
    @NotNull(message = "Task cannot be null.")
    private TaskEntity task;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Category cannot be null.")
    private Category category;

    // Constructors
    public TaskCategory() {}

    public TaskCategory(TaskEntity task, Category category) {
        this.task = task;
        this.category = category;
        this.id = new TaskCategoryId(task.getTaskid(), category.getCategoryId());
    }

    // Getters and Setters
    public TaskCategoryId getId() {
        return id;
    }

    public void setId(TaskCategoryId id) {
        this.id = id;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
