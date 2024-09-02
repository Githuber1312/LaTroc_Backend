package dev.artep.latroc_backend_simple.controller;

import dev.artep.latroc_backend_simple.model.Category;
import dev.artep.latroc_backend_simple.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerTest {

    private MockMvc mockMvc;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);
        CategoryController categoryController = new CategoryController(categoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void testGetAllCategories() throws Exception {
        Category category1 = new Category();
        Category category2 = new Category();
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{}, {}]"));

        verify(categoryService, times(1)).getAllCategories();
    }
}
