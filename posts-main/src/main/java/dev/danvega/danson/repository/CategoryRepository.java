package dev.danvega.danson.repository;

import dev.danvega.danson.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find category by name
    Optional<Category> findByCategoryName(String categoryName);

    // Check if a category exists by name
    boolean existsByCategoryName(String categoryName);
}
