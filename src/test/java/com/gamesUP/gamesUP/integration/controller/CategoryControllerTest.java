package com.gamesup.api.integration.controller;

import com.gamesup.api.config.WebMvcTestNoSecurity;
import com.gamesup.api.controller.CategoryController;
import com.gamesup.api.dto.CategoryDto;
import com.gamesup.api.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTestNoSecurity(controllers = CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    void getAllCategories_shouldReturnList() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of(new CategoryDto(1L, "Categorie1")));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].name").value("Categorie1"));
    }

    @Test
    void getCategoryById_shouldReturnCategory() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(new CategoryDto(1L, "Categorie1"));

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.name").value("Categorie1"));
    }
}