package com.noah.taskmanager.post;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.noah.taskmanager.config.TestSecurityConfig;
import com.noah.taskmanager.model.TaskEntity;
import com.noah.taskmanager.model.User;
import com.noah.taskmanager.model.enumEntity.Role;
import com.noah.taskmanager.model.enumEntity.TaskPriority;
import com.noah.taskmanager.model.enumEntity.TaskStatus;
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
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
@Testcontainers
@Import(TestSecurityConfig.class) // Import TestSecurityConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerIntegrationTest {

    @Container
    private static PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:16.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword")
            .withReuse(false); // Disable container reuse


    @LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port + "/api/tasks";
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Use the dynamically assigned URL, username, and password from the container
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @Test
    void testGetTasks() {
        String baseUrl = "http://localhost:" + port + "/api/tasks";
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

        System.out.println("Response: " + response.getBody());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void testGetTasksByUserId() {
        long userId = 1L;

        // Use the baseUrl method
        ResponseEntity<TaskEntity[]> response = restTemplate.getForEntity(baseUrl() + "/user/" + userId, TaskEntity[].class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testJsonSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        User user = new User(1L, "test@example.com", "password", Role.USER);
        TaskEntity task = new TaskEntity("Test Task", "Description", TaskPriority.HIGH, LocalDateTime.now(), TaskStatus.PENDING, user);

        String json = mapper.writeValueAsString(task);
        System.out.println("Serialized JSON: " + json);

        TaskEntity deserializedTask = mapper.readValue(json, TaskEntity.class);
        assertThat(deserializedTask).isNotNull();
    }

    @Test
    void testCreateTask() {
        // Generate a random email address
        String randomEmail = "test+" + UUID.randomUUID().toString() + "@example.com";

        // Step 1: Create a mock User object with a randomized email
        User mockUser = new User(1L, randomEmail, "password", Role.USER);

        // Step 2: Ensure the User exists in the database
        ResponseEntity<User> userResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/users", mockUser, User.class);

        // Step 3: Create a TaskEntity object with the mock User
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        TaskEntity task = new TaskEntity("Test Task", "Description", TaskPriority.HIGH, dueDate, TaskStatus.PENDING, userResponse.getBody());

        // Step 4: Send the POST request to create the Task
        ResponseEntity<TaskEntity> response = restTemplate.postForEntity(baseUrl(), task, TaskEntity.class);

        // Step 5: Assert the HTTP status and response body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTaskname()).isEqualTo("Test Task");

        // Additional Assertions: Validate other TaskEntity fields
        assertThat(response.getBody().getDescription()).isEqualTo("Description");
        assertThat(response.getBody().getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(response.getBody().getDuedate())
                .isCloseTo(dueDate, within(1, ChronoUnit.MINUTES));
        assertThat(response.getBody().getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(response.getBody().getUser().getEmail()).isEqualTo(mockUser.getEmail());

        // Step 7: Fetch and print all tasks
        ResponseEntity<TaskEntity[]> taskListResponse = restTemplate.getForEntity(baseUrl(), TaskEntity[].class);
        assertThat(taskListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        System.out.println("Tasks in the database:");
        if (taskListResponse.getBody() != null) {
            for (TaskEntity taskEntity : taskListResponse.getBody()) {
                System.out.println(taskEntity);
            }
        }
    }

    @Test
    void testUpdateTask() {
        // Step 0: Check that there are no tasks initially
        ResponseEntity<TaskEntity[]> initialTaskListResponse = restTemplate.getForEntity(baseUrl(), TaskEntity[].class);
        assertThat(initialTaskListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        System.out.println("Initial Tasks in the database:");
        if (initialTaskListResponse.getBody() != null && initialTaskListResponse.getBody().length > 0) {
            for (TaskEntity taskEntity : initialTaskListResponse.getBody()) {
                System.out.println(taskEntity);
            }
        } else {
            System.out.println("No tasks found.");
        }
        assertThat(initialTaskListResponse.getBody()).isEmpty(); // Assert database starts clean

        // Generate a random email address for the user
        String randomEmail = "test+" + UUID.randomUUID().toString() + "@example.com";

        // Step 1: Create a mock User object
        User mockUser = new User(1L, randomEmail, "password", Role.USER);

        // Step 2: Ensure the User exists in the database
        ResponseEntity<User> userResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/users", mockUser, User.class);

        // Step 3: Create a TaskEntity object with the mock User
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        TaskEntity task = new TaskEntity("Test Task", "Description", TaskPriority.HIGH, dueDate, TaskStatus.PENDING, userResponse.getBody());

        // Step 4: Send the POST request to create the Task
        ResponseEntity<TaskEntity> createResponse = restTemplate.postForEntity(baseUrl(), task, TaskEntity.class);

        // Step 5: Assert the HTTP status and response body
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();

        // Step 6: Prepare the updated TaskEntity object
        TaskEntity updatedTask = createResponse.getBody();
        assertThat(updatedTask).isNotNull(); // Ensure the task was created

        updatedTask.setTaskname("Updated Test Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setPriority(TaskPriority.LOW);
        updatedTask.setStatus(TaskStatus.COMPLETED);
        updatedTask.setDuedate(LocalDateTime.now().plusDays(2));

        // Step 7: Send the PUT request to update the Task
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskEntity> updateRequest = new HttpEntity<>(updatedTask, headers);

        ResponseEntity<TaskEntity> updateResponse = restTemplate.exchange(
                baseUrl() + "/" + updatedTask.getTaskid(),
                HttpMethod.PUT,
                updateRequest,
                TaskEntity.class
        );

        // Step 8: Assert the HTTP status and updated fields
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskEntity updatedResponseBody = updateResponse.getBody();
        assertThat(updatedResponseBody).isNotNull();
        assertThat(updatedResponseBody.getTaskname()).isEqualTo("Updated Test Task");
        assertThat(updatedResponseBody.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedResponseBody.getPriority()).isEqualTo(TaskPriority.LOW);
        assertThat(updatedResponseBody.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(updatedResponseBody.getDuedate()).isCloseTo(LocalDateTime.now().plusDays(2), within(1, ChronoUnit.MINUTES));

        // Step 9: Fetch and print all users
        ResponseEntity<User[]> userListResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/users", User[].class);
        assertThat(userListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        System.out.println("Users in the database:");
        if (userListResponse.getBody() != null) {
            for (User user : userListResponse.getBody()) {
                System.out.println(user);
            }
        }

        // Step 10: Fetch and print all tasks
        ResponseEntity<TaskEntity[]> taskListResponse = restTemplate.getForEntity(baseUrl(), TaskEntity[].class);
        assertThat(taskListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        System.out.println("Tasks in the database:");
        if (taskListResponse.getBody() != null) {
            for (TaskEntity taskEntity : taskListResponse.getBody()) {
                System.out.println(taskEntity);
            }
        }
    }

}

