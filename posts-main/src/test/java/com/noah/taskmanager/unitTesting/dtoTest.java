package com.noah.taskmanager.unitTesting;

import com.noah.taskmanager.dto.*;
import com.noah.taskmanager.model.*;
import com.noah.taskmanager.model.enumEntity.TaskPriority;
import com.noah.taskmanager.model.enumEntity.Role;
import com.noah.taskmanager.mapper.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DTOMapperUnitTest {

    private final TaskMapper taskMapper = new TaskMapper();
    private final UserMapper userMapper = new UserMapper();
    private final CategoryMapper categoryMapper = new CategoryMapper();
    private final NotificationMapper notificationMapper = new NotificationMapper();
    private final ReminderMapper reminderMapper = new ReminderMapper();

    @Test
    void testTaskDTOConversions() {
        // Test Create
        TaskCreateDTO createDTO = new TaskCreateDTO();
        createDTO.setTaskName("Test Task");
        createDTO.setDescription("Task Description");
        createDTO.setPriority("HIGH");
        createDTO.setDueDate(LocalDateTime.now());
        createDTO.setUserId(1L);

        TaskEntity taskEntity = taskMapper.toEntity(createDTO);
        assertThat(taskEntity.getTaskname()).isEqualTo("Test Task");
        assertThat(taskEntity.getDescription()).isEqualTo("Task Description");
        assertThat(taskEntity.getPriority()).isEqualTo(TaskPriority.HIGH);

        // Test Read
        TaskReadDTO readDTO = taskMapper.toDTO(taskEntity);
        assertThat(readDTO.getTaskName()).isEqualTo("Test Task");
        assertThat(readDTO.getDescription()).isEqualTo("Task Description");

        // Test Update
        TaskUpdateDTO updateDTO = new TaskUpdateDTO();
        updateDTO.setTaskName("Updated Task");
        updateDTO.setDescription("Updated Description");
        updateDTO.setPriority("LOW");

        taskMapper.updateEntityFromDTO(updateDTO, taskEntity);
        assertThat(taskEntity.getTaskname()).isEqualTo("Updated Task");
        assertThat(taskEntity.getDescription()).isEqualTo("Updated Description");
        assertThat(taskEntity.getPriority()).isEqualTo(TaskPriority.LOW);
    }

    @Test
    void testUserDTOConversions() {
        // Test Create
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setEmail("test@example.com");
        createDTO.setPassword("password");
        createDTO.setRole("USER");

        User userEntity = userMapper.toEntity(createDTO);
        assertThat(userEntity.getEmail()).isEqualTo("test@example.com");
        assertThat(userEntity.getRole()).isEqualTo(Role.USER);

        // Test Read
        UserReadDTO readDTO = userMapper.toDTO(userEntity);
        assertThat(readDTO.getEmail()).isEqualTo("test@example.com");
        assertThat(readDTO.getRole()).isEqualTo("USER");

        // Test Update
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setEmail("updated@example.com");
        updateDTO.setRole("ADMIN");

        userMapper.updateEntityFromDTO(updateDTO, userEntity);
        assertThat(userEntity.getEmail()).isEqualTo("updated@example.com");
        assertThat(userEntity.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void testCategoryDTOConversions() {
        // Test Create
        CategoryCreateDTO createDTO = new CategoryCreateDTO();
        createDTO.setCategoryName("Work");

        Category categoryEntity = categoryMapper.toEntity(createDTO);
        assertThat(categoryEntity.getCategoryName()).isEqualTo("Work");

        // Test Read
        CategoryReadDTO readDTO = categoryMapper.toDTO(categoryEntity);
        assertThat(readDTO.getCategoryName()).isEqualTo("Work");

        // Test Update
        CategoryUpdateDTO updateDTO = new CategoryUpdateDTO();
        updateDTO.setCategoryName("Personal");

        categoryMapper.updateEntityFromDTO(updateDTO, categoryEntity);
        assertThat(categoryEntity.getCategoryName()).isEqualTo("Personal");
    }

    @Test
    void testNotificationDTOConversions() {
        // Test Create
        NotificationCreateDTO createDTO = new NotificationCreateDTO();
        createDTO.setNotificationType("EMAIL");
        createDTO.setNotificationTime(LocalDateTime.now());
        createDTO.setTaskId(1L);

        Notification notificationEntity = notificationMapper.toEntity(createDTO);
        assertThat(notificationEntity.getNotificationType()).isEqualTo("EMAIL");

        // Mock related entities
        TaskEntity mockTask = new TaskEntity();
        mockTask.setTaskid(1L);

        User mockUser = new User(1L, "test@example.com", "password", Role.USER, null);

        notificationEntity.setTask(mockTask);
        notificationEntity.setUser(mockUser);

        // Test Read
        NotificationReadDTO readDTO = notificationMapper.toDTO(notificationEntity);
        assertThat(readDTO.getNotificationType()).isEqualTo("EMAIL");
        assertThat(readDTO.getNotificationTime()).isEqualTo(notificationEntity.getNotificationTime());
        assertThat(readDTO.getNotificationId()).isEqualTo(notificationEntity.getNotificationid());
        assertThat(readDTO.getUserId()).isEqualTo(mockUser.getUserid());
    }



    @Test
    void testReminderDTOConversions() {
        // Test Create
        ReminderCreateDTO createDTO = new ReminderCreateDTO();
        createDTO.setReminderTime(LocalDateTime.now());
        createDTO.setTaskId(1L);
        createDTO.setUserId(2L);

        Reminder reminderEntity = reminderMapper.toEntity(createDTO);
        assertThat(reminderEntity.getReminderTime()).isNotNull();

        // Mock related entities
        TaskEntity mockTask = new TaskEntity();
        mockTask.setTaskid(1L);

        User mockUser = new User(2L, "test@example.com", "password", Role.USER, null);

        reminderEntity.setTask(mockTask);
        reminderEntity.setUser(mockUser);

        // Test Read
        ReminderReadDTO readDTO = reminderMapper.toDTO(reminderEntity);
        assertThat(readDTO.getReminderTime()).isEqualTo(reminderEntity.getReminderTime());
        assertThat(readDTO.getTaskId()).isEqualTo(mockTask.getTaskid());
        assertThat(readDTO.getUserId()).isEqualTo(mockUser.getUserid());

        // Test Update
        ReminderUpdateDTO updateDTO = new ReminderUpdateDTO();
        updateDTO.setReminderTime(LocalDateTime.now().plusDays(1));

        reminderMapper.updateEntityFromDTO(updateDTO, reminderEntity);
        assertThat(reminderEntity.getReminderTime()).isEqualTo(updateDTO.getReminderTime());
    }


}
