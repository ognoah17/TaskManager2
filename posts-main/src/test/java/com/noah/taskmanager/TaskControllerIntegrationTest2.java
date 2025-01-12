package com.noah.taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.noah.taskmanager.config.TestSecurityConfig;
import com.noah.taskmanager.dto.TaskCreateDTO;
import com.noah.taskmanager.dto.TaskReadDTO;
import com.noah.taskmanager.dto.TaskUpdateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@Import(TestSecurityConfig.class)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:16.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    /**
     * Test GET /api/tasks.
     */
    @Test
    void testGetTasks() {
        ResponseEntity<TaskReadDTO[]> response = restTemplate.getForEntity(baseUrl("/api/tasks"), TaskReadDTO[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        System.out.println("Tasks: " + List.of(response.getBody()));
    }

    /**
     * Test GET /api/tasks/{id}.
     */
    @Test
    void testGetTaskById() {
        // Create a task
        TaskReadDTO createdTask = createTaskForTest();

        // Retrieve the task
        ResponseEntity<TaskReadDTO> response = restTemplate.getForEntity(baseUrl("/api/tasks/" + createdTask.getTaskId()), TaskReadDTO.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTaskId()).isEqualTo(createdTask.getTaskId());
        System.out.println("Retrieved Task: " + response.getBody());
    }

    /**
     * Test POST /api/tasks.
     */
    @Test
    void testCreateTask() {
        TaskReadDTO createdTask = createTaskForTest();
        assertThat(createdTask).isNotNull();
    }

    /**
     * Test PUT /api/tasks/{id}.
     */
    @Test
    void testUpdateTask() {
        // Create a task
        TaskReadDTO createdTask = createTaskForTest();

        // Update details
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setTaskName("Updated Task Name");
        taskUpdateDTO.setPriority("LOW");
        taskUpdateDTO.setDescription("Updated Description");
        taskUpdateDTO.setStatus("COMPLETED");

        // Send update request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskUpdateDTO> request = new HttpEntity<>(taskUpdateDTO, headers);

        ResponseEntity<TaskReadDTO> response = restTemplate.exchange(
                baseUrl("/api/tasks/" + createdTask.getTaskId()),
                HttpMethod.PUT,
                request,
                TaskReadDTO.class
        );

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTaskName()).isEqualTo("Updated Task Name");
        assertThat(response.getBody().getPriority()).isEqualTo("LOW");
        System.out.println("Updated Task: " + response.getBody());
    }

    /**
     * Test DELETE /api/tasks/{id}.
     */
    @Test
    void testDeleteTask() {
        // Create a task
        TaskReadDTO createdTask = createTaskForTest();
        System.out.println("Created Task ID: " + createdTask.getTaskId());

        // Delete the task
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl("/api/tasks/" + createdTask.getTaskId()),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Verify deletion response
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        System.out.println("Deleted Task ID: " + createdTask.getTaskId());

        // Attempt to retrieve the deleted task
        ResponseEntity<TaskReadDTO> getResponse = restTemplate.getForEntity(
                baseUrl("/api/tasks/" + createdTask.getTaskId()),
                TaskReadDTO.class
        );

        // Verify the task is not found
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        System.out.println("Task ID " + createdTask.getTaskId() + " not found as expected after deletion.");
    }


    /**
     * Helper method to create a task for testing.
     */
    private TaskReadDTO createTaskForTest() {
        // Create a TaskCreateDTO
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setTaskName("Test Task");
        taskCreateDTO.setDescription("This is a test task");
        taskCreateDTO.setPriority("HIGH");
        taskCreateDTO.setDueDate(LocalDateTime.now().plusDays(1));
        taskCreateDTO.setUserId(1L);

        // Send the POST request
        ResponseEntity<TaskReadDTO> response = restTemplate.postForEntity(baseUrl("/api/tasks"), taskCreateDTO, TaskReadDTO.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TaskReadDTO responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        System.out.println("Created Task: " + responseBody);
        return responseBody;
    }
}
