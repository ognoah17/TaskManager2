package com.noah.taskmanager.integrationtesting;

import com.noah.taskmanager.config.TestSecurityConfig;
import com.noah.taskmanager.dto.CategoryCreateDTO;
import com.noah.taskmanager.dto.CategoryReadDTO;
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
class CategoryControllerIntegrationTest {

    // Static PostgreSQL container, reused across tests
    private static PostgreSQLContainer<?> POSTGRES_CONTAINER;

    @BeforeAll
    static void setUpContainer() {
        // Always start a new container with a unique database name each time
        String uniqueDatabaseName = "testdb_" + UUID.randomUUID();
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
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categories", Integer.class);
        assertThat(count).isEqualTo(0); // Ensure no data is persisted before the test runs
    }

    @AfterEach
    void cleanup() {
        // Optionally clean up by truncating the 'categories' table to ensure no data persists between tests
        jdbcTemplate.update("TRUNCATE TABLE categories CASCADE");
    }

    @Test
    void testCreateCategory() {
        // Use the helper method to create a category
        CategoryReadDTO createdCategory = createCategoryForTest();
        assertThat(createdCategory).isNotNull();

        // Verify that the category was persisted in the database
        String categoryName = createdCategory.getCategoryName();
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM categories WHERE categoryname = ?", Integer.class, categoryName);
        assertThat(count).isEqualTo(1); // Ensure the category is persisted

        // Print all categories in the 'categories' table
        String query = "SELECT * FROM categories";
        List<Map<String, Object>> categories = jdbcTemplate.queryForList(query);

        System.out.println("All Categories in the database:");
        for (Map<String, Object> category : categories) {
            System.out.println(category);
        }
    }

    @Test
    void testGetCategoryById() {
        // Use the helper method to create a category
        CategoryReadDTO createdCategory = createCategoryForTest();
        assertThat(createdCategory).isNotNull();

        // Retrieve the category by ID
        ResponseEntity<CategoryReadDTO> response = restTemplate.getForEntity(baseUrl("/api/categories/" + createdCategory.getCategoryId()), CategoryReadDTO.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCategoryId()).isEqualTo(createdCategory.getCategoryId());
    }

    private CategoryReadDTO createCategoryForTest() {
        // Generate a unique category name using UUID
        String uniqueCategoryName = "TestCategory_" + UUID.randomUUID();

        // Create a CategoryCreateDTO object
        CategoryCreateDTO categoryCreateDTO = new CategoryCreateDTO();
        categoryCreateDTO.setCategoryName(uniqueCategoryName);

        // Send the POST request to create a new category
        ResponseEntity<CategoryReadDTO> response = restTemplate.postForEntity(baseUrl("/api/categories"), categoryCreateDTO, CategoryReadDTO.class);

        // Assertions
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        // Print created category
        System.out.println("Created Category: " + response.getBody());

        return response.getBody();
    }
}
