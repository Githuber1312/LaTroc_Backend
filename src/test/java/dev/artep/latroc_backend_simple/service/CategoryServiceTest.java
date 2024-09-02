package dev.artep.latroc_backend_simple.service;

import dev.artep.latroc_backend_simple.model.Category;
import dev.artep.latroc_backend_simple.repository.LocalCategoryDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    private CategoryService categoryService;
    private LocalCategoryDAO categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(LocalCategoryDAO.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void testGetAllCategories() {
        Category category1 = new Category();
        Category category2 = new Category();

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }
}
