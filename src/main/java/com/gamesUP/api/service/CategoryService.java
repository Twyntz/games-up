package com.gamesup.api.service;

import com.gamesup.api.dto.CategoryDto;
import com.gamesup.api.dto.CreateCategoryDto;
import com.gamesup.api.dto.UpdateCategoryDto;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.Category;
import com.gamesup.api.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(a -> new CategoryDto(a.getId(), a.getName()))
                .toList();
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("On trouve pas la catégorie à afficher"));
        return new CategoryDto(category.getId(), category.getName());
    }

    public CategoryDto createCategory(CreateCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.name());
        Category saved = categoryRepository.save(category);
        return new CategoryDto(saved.getId(), saved.getName());
    }

    public CategoryDto updateCategory(Long id, UpdateCategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("On trouve pas la catégorie à modifier"));
        category.setName(dto.name());
        Category updated = categoryRepository.save(category);
        return new CategoryDto(updated.getId(), updated.getName());
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("On trouve pas la catégorie à supprimer"));
        categoryRepository.deleteById(category.getId());
    }
}