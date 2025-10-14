package com.gamesup.api.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesup.api.config.WebMvcTestNoSecurity;
import com.gamesup.api.controller.WishlistController;
import com.gamesup.api.dto.wishlist.CreateWishlistDto;
import com.gamesup.api.dto.wishlist.WishlistDto;
import com.gamesup.api.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTestNoSecurity(controllers = WishlistController.class)
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishlistService wishlistService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getWishlist_shouldReturnList() throws Exception {
        WishlistDto dto = new WishlistDto(1L, 1L, 1L, "Game1");

        Mockito.when(wishlistService.getWishlist(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/wishlist")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].gameName").value("Game1"))
                .andExpect(jsonPath("$.data[0].userId").value(1));
    }

    @Test
    void addToWishlist_shouldReturnCreated() throws Exception {
        CreateWishlistDto createDto = new CreateWishlistDto(1L, 1L);
        WishlistDto responseDto = new WishlistDto(10L, 1L, 1L, "Game1");

        Mockito.when(wishlistService.addToWishlist(1L, 1L)).thenReturn(responseDto);

        mockMvc.perform(post("/wishlist")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.id").value(10))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.gameName").value("Game1"));
    }

    @Test
    void removeFromWishlist_shouldReturnSuccess() throws Exception {
        doNothing().when(wishlistService).removeFromWishlist(1L, 1L);

        mockMvc.perform(delete("/wishlist")
                        .param("userId", "1")
                        .param("gameId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Jeu supprimé de la liste de souhaits avec succès"));
    }
}