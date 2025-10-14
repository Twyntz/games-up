package com.gamesup.api.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesup.api.config.WebMvcTestNoSecurity;
import com.gamesup.api.controller.PublisherAdminController;
import com.gamesup.api.dto.PublisherDto;
import com.gamesup.api.dto.CreatePublisherDto;
import com.gamesup.api.dto.UpdatePublisherDto;
import com.gamesup.api.service.PublisherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTestNoSecurity(controllers = PublisherAdminController.class)
class PublisherAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPublisher_shouldReturnCreated() throws Exception {
        CreatePublisherDto dto = new CreatePublisherDto("Nouvel éditeur");
        PublisherDto response = new PublisherDto(1L, "Nouvel éditeur");

        Mockito.when(publisherService.createPublisher(any(CreatePublisherDto.class))).thenReturn(response);

        mockMvc.perform(post("/admin/publishers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Nouvel éditeur"));
    }

    @Test
    void updatePublisher_shouldReturnUpdated() throws Exception {
        UpdatePublisherDto dto = new UpdatePublisherDto("Éditeur mis à jour");
        PublisherDto response = new PublisherDto(1L, "Éditeur mis à jour");

        Mockito.when(publisherService.updatePublisher(Mockito.eq(1L), any(UpdatePublisherDto.class))).thenReturn(response);

        mockMvc.perform(put("/admin/publishers/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Éditeur mis à jour"));
    }

    @Test
    void deletePublisher_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/admin/publishers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Éditeur supprimé avec succès"));
    }
}