package com.gamesup.api.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesup.api.config.WebMvcTestNoSecurity;
import com.gamesup.api.controller.CategoryAdminController;
import com.gamesup.api.dto.CategoryDto;
import com.gamesup.api.dto.CreateCategoryDto;
import com.gamesup.api.dto.UpdateCategoryDto;
import com.gamesup.api.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTestNoSecurity(controllers = CategoryAdminController.class)
class CategoryAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCategory_shouldReturnCreated() throws Exception {
        CreateCategoryDto dto = new CreateCategoryDto("Action");
        CategoryDto response = new CategoryDto(1L, "Action");

        Mockito.when(categoryService.createCategory(any(CreateCategoryDto.class))).thenReturn(response);

        mockMvc.perform(post("/admin/categories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Action"));
    }

    @Test
    void updateCategory_shouldReturnUpdated() throws Exception {
        UpdateCategoryDto dto = new UpdateCategoryDto("Aventure");
        CategoryDto response = new CategoryDto(1L, "Aventure");

        Mockito.when(categoryService.updateCategory(Mockito.eq(1L), any(UpdateCategoryDto.class))).thenReturn(response);

        mockMvc.perform(put("/admin/categories/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Aventure"));
    }

    @Test
    void deleteCategory_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/admin/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Catégorie dans le void"));
    }
}