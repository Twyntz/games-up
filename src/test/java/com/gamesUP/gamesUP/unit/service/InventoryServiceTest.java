package com.gamesup.api.unit.service;

import com.gamesup.api.dto.inventory.CreateInventoryDto;
import com.gamesup.api.dto.inventory.UpdateInventoryDto;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.Game;
import com.gamesup.api.model.Inventory;
import com.gamesup.api.repository.GameRepository;
import com.gamesup.api.repository.InventoryRepository;
import com.gamesup.api.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    private InventoryRepository inventoryRepository;
    private GameRepository gameRepository;
    private InventoryService inventoryService;

    private final Game sampleGame = new Game();
    private final Inventory sampleInventory = new Inventory();

    @BeforeEach
    void setUp() {
        inventoryRepository = mock(InventoryRepository.class);
        gameRepository = mock(GameRepository.class);
        inventoryService = new InventoryService(inventoryRepository, gameRepository);

        sampleGame.setId(1L);
        sampleGame.setName("Game1");

        sampleInventory.setId(1L);
        sampleInventory.setStock(10);
        sampleInventory.setGame(sampleGame);
    }

    @Test
    void getAllInventories_shouldReturnList() {
        when(inventoryRepository.findAll()).thenReturn(List.of(sampleInventory));

        var result = inventoryService.getAllInventories();

        assertEquals(1, result.size());
        assertEquals(10, result.getFirst().stock());
        assertEquals("Game1", result.getFirst().gameName());
    }

    @Test
    void getInventoryById_shouldReturnInventory() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(sampleInventory));

        var result = inventoryService.getInventoryById(1L);

        assertEquals(10, result.stock());
        assertEquals(1L, result.gameId());
    }

    @Test
    void getInventoryById_shouldThrowIfNotFound() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.getInventoryById(1L));
    }

    @Test
    void createInventory_shouldSaveAndReturnDto() {
        CreateInventoryDto dto = new CreateInventoryDto(1L, 25);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(sampleGame));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(sampleInventory);

        var result = inventoryService.createInventory(dto);

        assertEquals(10, result.stock());
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void createInventory_shouldThrowIfGameNotFound() {
        CreateInventoryDto dto = new CreateInventoryDto(99L, 25);
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.createInventory(dto));
    }

    @Test
    void updateInventory_shouldUpdateAndReturnDto() {
        UpdateInventoryDto dto = new UpdateInventoryDto(1L, 50);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(sampleInventory));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(sampleGame));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(sampleInventory);

        var result = inventoryService.updateInventory(1L, dto);

        assertEquals(50, result.stock());
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void updateInventory_shouldThrowIfInventoryNotFound() {
        UpdateInventoryDto dto = new UpdateInventoryDto(1L, 50);
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.updateInventory(1L, dto));
    }

    @Test
    void updateInventory_shouldThrowIfGameNotFound() {
        UpdateInventoryDto dto = new UpdateInventoryDto(1L, 50);
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(sampleInventory));
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.updateInventory(1L, dto));
    }

    @Test
    void deleteInventory_shouldDeleteIfExists() {
        when(inventoryRepository.existsById(1L)).thenReturn(true);

        inventoryService.deleteInventory(1L);

        verify(inventoryRepository).deleteById(1L);
    }

    @Test
    void deleteInventory_shouldThrowIfNotExists() {
        when(inventoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.deleteInventory(1L));
    }
}