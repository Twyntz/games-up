package com.gamesup.api.integration.controller;

import com.gamesup.api.config.WebMvcTestNoSecurity;
import com.gamesup.api.controller.GameController;
import com.gamesup.api.dto.game.GameDto;
import com.gamesup.api.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTestNoSecurity(controllers = GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    void getAllGames_shouldReturnList() throws Exception {
        GameDto game = new GameDto(
                1L,
                "Game1",
                new BigDecimal("39.99"),
                LocalDate.of(2020, 1, 1),
                "Auteur1",
                "Categorie1",
                "Editeur1"
        );

        when(gameService.getAllGames()).thenReturn(List.of(game));

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].name").value("Game1"))
                .andExpect(jsonPath("$.data[0].authorName").value("Auteur1"));
    }

    @Test
    void getGameById_shouldReturnGame() throws Exception {
        GameDto game = new GameDto(
                1L,
                "Game1",
                new BigDecimal("39.99"),
                LocalDate.of(2020, 1, 1),
                "Auteur1",
                "Categorie1",
                "Editeur1"
        );

        when(gameService.getGameById(1L)).thenReturn(game);

        mockMvc.perform(get("/games/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.name").value("Game1"))
                .andExpect(jsonPath("$.data.publisherName").value("Editeur1"));
    }
}