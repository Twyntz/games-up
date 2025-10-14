package com.gamesup.api.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesup.api.config.WebMvcTestNoSecurity;
import com.gamesup.api.controller.InventoryAdminController;
import com.gamesup.api.dto.inventory.CreateInventoryDto;
import com.gamesup.api.dto.inventory.InventoryDto;
import com.gamesup.api.dto.inventory.UpdateInventoryDto;
import com.gamesup.api.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTestNoSecurity(controllers = InventoryAdminController.class)
class InventoryAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createInventory_shouldReturnCreated() throws Exception {
        CreateInventoryDto dto = new CreateInventoryDto(1L, 15);
        InventoryDto response = new InventoryDto(1L, 15, 1L, "Game1");

        Mockito.when(inventoryService.createInventory(any(CreateInventoryDto.class))).thenReturn(response);

        mockMvc.perform(post("/admin/inventories")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.stock").value(15))
                .andExpect(jsonPath("$.data.gameName").value("Game1"));
    }

    @Test
    void updateInventory_shouldReturnUpdated() throws Exception {
        UpdateInventoryDto dto = new UpdateInventoryDto(1L, 25);
        InventoryDto response = new InventoryDto(1L, 25, 1L, "Game1");

        Mockito.when(inventoryService.updateInventory(Mockito.eq(1L), any(UpdateInventoryDto.class))).thenReturn(response);

        mockMvc.perform(put("/admin/inventories/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.stock").value(25))
                .andExpect(jsonPath("$.data.gameName").value("Game1"));
    }

    @Test
    void deleteInventory_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/admin/inventories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Inventaire supprimé avec succès"));
    }
}