package com.gamesup.api.integration.controller;

import com.gamesup.api.config.WebMvcTestNoSecurity;
import com.gamesup.api.controller.PublisherController;
import com.gamesup.api.dto.PublisherDto;
import com.gamesup.api.service.PublisherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTestNoSecurity(controllers = PublisherController.class)
class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @Test
    void getAllPublishers_shouldReturnList() throws Exception {
        when(publisherService.getAllPublishers()).thenReturn(List.of(new PublisherDto(1L, "Editeur1")));

        mockMvc.perform(get("/publishers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].name").value("Editeur1"));
    }

    @Test
    void getPublisherById_shouldReturnPublisher() throws Exception {
        when(publisherService.getPublisherById(1L)).thenReturn(new PublisherDto(1L, "Editeur1"));

        mockMvc.perform(get("/publishers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.name").value("Editeur1"));
    }
}