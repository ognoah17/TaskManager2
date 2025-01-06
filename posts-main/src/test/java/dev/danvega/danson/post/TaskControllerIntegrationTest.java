package dev.danvega.danson.post;

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
import java.time.temporal.ChronoUnit;
import java.util.UUID;

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
}

