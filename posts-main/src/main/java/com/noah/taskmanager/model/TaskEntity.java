package com.noah.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.noah.taskmanager.model.enumEntity.TaskPriority;
import com.noah.taskmanager.model.enumEntity.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks", schema = "public",
        indexes = {
                @Index(name = "idx_priority", columnList = "priority"),
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_due_date", columnList = "duedate")
        })
@NamedQueries({
        @NamedQuery(name = "TaskEntity.findByStatus", query = "SELECT t FROM TaskEntity t WHERE t.status = :status"),
        @NamedQuery(name = "TaskEntity.findByPriority", query = "SELECT t FROM TaskEntity t WHERE t.priority = :priority"),
        @NamedQuery(name = "TaskEntity.findByUser", query = "SELECT t FROM TaskEntity t WHERE t.user.userid = :userId")
})
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskid;

    @NotNull(message = "Task name cannot be null.")
    @Size(min = 1, max = 255, message = "Task name must be between 1 and 255 characters.")
    @Column(nullable = false, name = "taskname")
    private String taskname;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters.")
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Priority cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @NotNull(message = "Due date cannot be null.")
    @FutureOrPresent(message = "Due date must be in the future or present.")
    @Column(nullable = false, name = "due_date")
    private LocalDateTime duedate;

    @NotNull(message = "Status cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "task_categories",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    // Constructors
    public TaskEntity() {
        // Default constructor for JPA
    }

    public TaskEntity(String taskname, String description, TaskPriority priority, LocalDateTime duedate, TaskStatus status, User user) {
        this.taskname = taskname;
        this.description = description;
        this.priority = priority;
        this.duedate = duedate;
        this.status = status;
        this.user = user;
    }

    // Lifecycle Callbacks
    @PrePersist
    @PreUpdate
    private void setDefaultValues() {
        if (priority == null) {
            this.priority = TaskPriority.MEDIUM;
        }
        if (status == null) {
            this.status = TaskStatus.PENDING;
        }
    }

    // Helper Methods for Collections
    public void initializeCategories() {
        if (categories == null) {
            categories = new HashSet<>();
        }
    }

    // Getters and Setters
    public Long getTaskid() {
        return taskid;
    }

    public void setTaskid(Long taskid) {
        this.taskid = taskid;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDuedate() {
        return duedate;
    }

    public void setDuedate(LocalDateTime duedate) {
        this.duedate = duedate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return STR."TaskEntity{taskid=\{taskid}, taskname='\{taskname}', duedate=\{duedate}}";
    }
}
