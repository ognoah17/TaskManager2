package com.noah.taskmanager.mapper;

import com.noah.taskmanager.dto.TaskCreateDTO;
import com.noah.taskmanager.dto.TaskReadDTO;
import com.noah.taskmanager.dto.TaskUpdateDTO;
import com.noah.taskmanager.model.TaskEntity;
import com.noah.taskmanager.model.enumEntity.TaskPriority;
import com.noah.taskmanager.model.enumEntity.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskEntity toEntity(TaskCreateDTO dto) {
        TaskEntity task = new TaskEntity();
        task.setTaskname(dto.getTaskName());
        task.setDescription(dto.getDescription());
        task.setPriority(TaskPriority.valueOf(dto.getPriority()));
        task.setDuedate(dto.getDueDate());
        task.setStatus(TaskStatus.PENDING); // Default to PENDING
        return task;
    }

    public TaskReadDTO toDTO(TaskEntity task) {
        return new TaskReadDTO(
                task.getTaskid(),
                task.getTaskname(),
                task.getDescription(),
                task.getPriority().toString(),
                task.getStatus().toString(),
                task.getDuedate(),
                task.getUser() != null ? task.getUser().getUserid() : null
        );
    }

    public void updateEntityFromDTO(TaskUpdateDTO dto, TaskEntity task) {
        if (dto.getTaskName() != null) task.setTaskname(dto.getTaskName());
        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        if (dto.getPriority() != null) task.setPriority(TaskPriority.valueOf(dto.getPriority()));
        if (dto.getStatus() != null) task.setStatus(TaskStatus.valueOf(dto.getStatus()));
        if (dto.getDueDate() != null) task.setDuedate(dto.getDueDate());
    }
}
