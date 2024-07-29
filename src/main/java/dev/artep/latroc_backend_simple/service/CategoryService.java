package dev.artep.latroc_backend_simple.service;

import dev.artep.latroc_backend_simple.model.Category;
import dev.artep.latroc_backend_simple.repository.LocalCategoryDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {
    private final LocalCategoryDAO categoryRepository;

    public CategoryService(LocalCategoryDAO localCategoryDAO) {
        this.categoryRepository = localCategoryDAO;
    }

    public List<Category> getAllCategories() {
        Iterable<Category> iterable = categoryRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}
