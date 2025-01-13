package com.noah.taskmanager.integrationtesting;

import com.noah.taskmanager.config.TestSecurityConfig;
import com.noah.taskmanager.dto.UserCreateDTO;
import com.noah.taskmanager.dto.UserReadDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)
class UserControllerIntegrationTest {

    // Static PostgreSQL container, reused across tests
    private static PostgreSQLContainer<?> POSTGRES_CONTAINER;

    @BeforeAll
    static void setUpContainer() {
        // Always start a new container with a unique database name each time
        String uniqueDatabaseName = "testdb_" + UUID.randomUUID().toString();
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:16.0")
                .withDatabaseName(uniqueDatabaseName)
                .withUsername("testuser")
                .withPassword("testpassword");

        POSTGRES_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @BeforeEach
    void setup() {
        // Verify that the database is empty before starting each test
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        assertThat(count).isEqualTo(0);  // Ensure no data is persisted before the test runs
    }

    @AfterEach
    void cleanup() {
        // Optionally clean up by truncating the 'users' table to ensure no data persists between tests
        jdbcTemplate.update("TRUNCATE TABLE users CASCADE");
    }

    @Test
    void testCreateUser() {
        // Use the helper method to create a user
        UserReadDTO createdUser = createUserForTest();
        assertThat(createdUser).isNotNull();

        // Verify that the user was persisted in the database
        String email = createdUser.getEmail();
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE email = ?", Integer.class, email);
        assertThat(count).isEqualTo(1);  // Ensure the user is persisted

        // Print all users in the 'users' table
        String query = "SELECT * FROM users";
        List<Map<String, Object>> users = jdbcTemplate.queryForList(query);

        System.out.println("All Users in the database:");
        for (Map<String, Object> user : users) {
            System.out.println(user);
        }
    }


    @Test
    void testGetUserByEmail() {
        // Use the helper method to create a user
        UserReadDTO createdUser = createUserForTest();
        assertThat(createdUser).isNotNull();

        // Retrieve the user by email
        ResponseEntity<UserReadDTO> response = restTemplate.getForEntity(baseUrl("/api/users/" + createdUser.getEmail()), UserReadDTO.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(createdUser.getEmail());
    }

    private UserReadDTO createUserForTest() {
        // Generate a unique identifier using UUID
        String uniqueIdentifier = UUID.randomUUID().toString();

        // Generate a unique email and username using UUID
        String uniqueEmail = "test" + uniqueIdentifier + "@example.com";
        String uniqueUsername = "TestUser" + uniqueIdentifier;

        // Create a UserCreateDTO object
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setUsername(uniqueUsername);
        userCreateDTO.setEmail(uniqueEmail);
        userCreateDTO.setPassword("password123");
        userCreateDTO.setRole("USER");

        // Send the POST request to create a new user
        ResponseEntity<UserReadDTO> response = restTemplate.postForEntity(baseUrl("/api/users"), userCreateDTO, UserReadDTO.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        // Print created user
        System.out.println("Created User: " + response.getBody());

        return response.getBody();
    }
}
