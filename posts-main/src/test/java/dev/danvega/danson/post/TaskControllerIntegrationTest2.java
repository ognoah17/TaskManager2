package dev.danvega.danson.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.danvega.danson.config.TestSecurityConfig;
import dev.danvega.danson.model.TaskEntity;
import dev.danvega.danson.model.User;
import dev.danvega.danson.model.enumEntity.Role;
import dev.danvega.danson.model.enumEntity.TaskPriority;
import dev.danvega.danson.model.enumEntity.TaskStatus;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@Import(TestSecurityConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerIntegrationTest2 {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:16.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword")
            .withReuse(false);

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @Test
    void testJsonSerialization() throws Exception {
        User user = new User(1L, "test@example.com", "password", Role.USER);
        TaskEntity task = new TaskEntity("Test Task", "Description", TaskPriority.HIGH, LocalDateTime.now(), TaskStatus.PENDING, user);

        String json = objectMapper.writeValueAsString(task);
        System.out.println("Serialized JSON: " + json);

        TaskEntity deserializedTask = objectMapper.readValue(json, TaskEntity.class);
        assertThat(deserializedTask).isNotNull();
    }

    @Test
    void testCreateTask() {
        String randomEmail = "test+" + UUID.randomUUID() + "@example.com";

        // Step 1: Create a mock User object
        User mockUser = new User(1L, randomEmail, "password", Role.USER);
        ResponseEntity<User> userResponse = restTemplate.postForEntity(baseUrl("/api/users"), mockUser, User.class);

        // Step 2: Create a TaskEntity object with the mock User
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        TaskEntity task = new TaskEntity("Test Task", "Description", TaskPriority.HIGH, dueDate, TaskStatus.PENDING, userResponse.getBody());

        // Step 3: Send the POST request to create the Task
        ResponseEntity<TaskEntity> response = restTemplate.postForEntity(baseUrl("/api/tasks"), task, TaskEntity.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        System.out.println("Created Task: " + response.getBody());
    }

    @Test
    void testUpdateTask() {
        // Step 0: Ensure no tasks exist initially
        ResponseEntity<TaskEntity[]> initialTaskResponse = restTemplate.getForEntity(baseUrl("/api/tasks"), TaskEntity[].class);
        assertThat(initialTaskResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(initialTaskResponse.getBody()).isEmpty();

        // Step 1: Create a User
        String randomEmail = "test+" + UUID.randomUUID() + "@example.com";
        User mockUser = new User(1L, randomEmail, "password", Role.USER);
        ResponseEntity<User> userResponse = restTemplate.postForEntity(baseUrl("/api/users"), mockUser, User.class);

        // Step 2: Create a TaskEntity
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        TaskEntity task = new TaskEntity("Test Task", "Description", TaskPriority.HIGH, dueDate, TaskStatus.PENDING, userResponse.getBody());
        ResponseEntity<TaskEntity> createResponse = restTemplate.postForEntity(baseUrl("/api/tasks"), task, TaskEntity.class);
        TaskEntity createdTask = createResponse.getBody();

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createdTask).isNotNull();

        // Step 3: Update the TaskEntity
        createdTask.setTaskname("Updated Task Name");
        createdTask.setPriority(TaskPriority.LOW);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskEntity> updateRequest = new HttpEntity<>(createdTask, headers);

        ResponseEntity<TaskEntity> updateResponse = restTemplate.exchange(
                baseUrl("/api/tasks/" + createdTask.getTaskid()),
                HttpMethod.PUT,
                updateRequest,
                TaskEntity.class
        );

        // Assertions
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();

        System.out.println("Updated Task: " + updateResponse.getBody());
    }
}
