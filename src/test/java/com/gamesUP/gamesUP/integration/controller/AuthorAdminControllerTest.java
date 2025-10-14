package com.gamesup.api.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesup.api.config.WebMvcTestNoSecurity;
import com.gamesup.api.controller.AuthorAdminController;
import com.gamesup.api.dto.AuthorDto;
import com.gamesup.api.dto.CreateAuthorDto;
import com.gamesup.api.dto.UpdateAuthorDto;
import com.gamesup.api.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTestNoSecurity(controllers = AuthorAdminController.class)
class AuthorAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAuthor_shouldReturnCreated() throws Exception {
        CreateAuthorDto dto = new CreateAuthorDto("New Author");
        AuthorDto response = new AuthorDto(1L, "New Author");

        Mockito.when(authorService.createAuthor(any(CreateAuthorDto.class))).thenReturn(response);

        mockMvc.perform(post("/admin/authors")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("New Author"));
    }

    @Test
    void updateAuthor_shouldReturnUpdated() throws Exception {
        UpdateAuthorDto dto = new UpdateAuthorDto("Updated Author");
        AuthorDto response = new AuthorDto(1L, "Updated Author");

        Mockito.when(authorService.updateAuthor(Mockito.eq(1L), any(UpdateAuthorDto.class))).thenReturn(response);

        mockMvc.perform(put("/admin/authors/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Author"));
    }

    @Test
    void deleteAuthor_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/admin/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Author deleted successfully"));
    }
}