package dev.artep.latroc_backend_simple.repository;

import dev.artep.latroc_backend_simple.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LocalCategoryDAO extends CrudRepository<Category, Long> {
    Optional<Category> findByNameIgnoreCase(String name);
}
