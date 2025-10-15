package com.gamesup.api.unit.service;

import com.gamesup.api.dto.CategoryDto;
import com.gamesup.api.dto.CreateCategoryDto;
import com.gamesup.api.dto.UpdateCategoryDto;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.Category;
import com.gamesup.api.repository.CategoryRepository;
import com.gamesup.api.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void getAllCategories_shouldReturnList() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(
                new Category(1L, "Categorie1", null),
                new Category(2L, "Categorie2", null)
        ));

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Categorie1", result.getFirst().name());
    }

    @Test
    void getCategoryById_shouldReturnCategory() {
        Category category = new Category(1L, "Categorie1", null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDto result = categoryService.getCategoryById(1L);

        assertEquals("Categorie1", result.name());
    }

    @Test
    void getCategoryById_shouldThrowIfNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void createCategory_shouldSaveAndReturnDto() {
        CreateCategoryDto dto = new CreateCategoryDto("Action");
        Category saved = new Category(1L, "Action", null);

        when(categoryRepository.save(any(Category.class))).thenReturn(saved);

        CategoryDto result = categoryService.createCategory(dto);

        assertEquals("Action", result.name());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateCategory_shouldUpdateName() {
        Category existing = new Category(1L, "Vieux nom", null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(existing);

        UpdateCategoryDto dto = new UpdateCategoryDto("Nouveau nom");
        CategoryDto result = categoryService.updateCategory(1L, dto);

        assertEquals("Nouveau nom", result.name());
    }

    @Test
    void deleteCategory_shouldCallDelete() {
        Category existing = new Category(1L, "Go void", null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));

        categoryService.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }
}