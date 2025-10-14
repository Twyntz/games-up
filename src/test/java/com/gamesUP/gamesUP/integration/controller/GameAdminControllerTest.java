package com.gamesup.api.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesup.api.config.WebMvcTestNoSecurity;
import com.gamesup.api.controller.GameAdminController;
import com.gamesup.api.dto.game.CreateGameDto;
import com.gamesup.api.dto.game.GameDto;
import com.gamesup.api.dto.game.UpdateGameDto;
import com.gamesup.api.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTestNoSecurity(controllers = GameAdminController.class)
class GameAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createGame_shouldReturnCreated() throws Exception {
        CreateGameDto dto = new CreateGameDto(
                "Jeu1",
                new BigDecimal("39.99"),
                LocalDate.of(2020, 1, 1),
                1L,
                2L,
                3L
        );

        GameDto response = new GameDto(
                1L,
                "Jeu1",
                new BigDecimal("39.99"),
                LocalDate.of(2020, 1, 1),
                "Auteur1",
                "Categorie1",
                "Editeur1"
        );

        Mockito.when(gameService.createGame(any(CreateGameDto.class))).thenReturn(response);

        mockMvc.perform(post("/admin/games")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Jeu1"))
                .andExpect(jsonPath("$.data.price").value(39.99));
    }

    @Test
    void updateGame_shouldReturnUpdated() throws Exception {
        UpdateGameDto dto = new UpdateGameDto(
                "Updated Game",
                new BigDecimal("49.99"),
                LocalDate.of(2021, 5, 10),
                1L,
                2L,
                3L
        );

        GameDto response = new GameDto(
                1L,
                "Updated Game",
                new BigDecimal("49.99"),
                LocalDate.of(2021, 5, 10),
                "Auteur1",
                "Categorie1",
                "Editeur1"
        );

        Mockito.when(gameService.updateGame(Mockito.eq(1L), any(UpdateGameDto.class))).thenReturn(response);

        mockMvc.perform(put("/admin/games/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Game"))
                .andExpect(jsonPath("$.data.price").value(49.99));
    }

    @Test
    void deleteGame_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/admin/games/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Game deleted successfully"));
    }
}