package com.gamesup.api.service;

import com.gamesup.api.dto.inventory.CreateInventoryDto;
import com.gamesup.api.dto.inventory.InventoryDto;
import com.gamesup.api.dto.inventory.UpdateInventoryDto;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.Game;
import com.gamesup.api.model.Inventory;
import com.gamesup.api.repository.GameRepository;
import com.gamesup.api.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final GameRepository gameRepository;

    public InventoryService(InventoryRepository inventoryRepository, GameRepository gameRepository) {
        this.inventoryRepository = inventoryRepository;
        this.gameRepository = gameRepository;
    }

    public List<InventoryDto> getAllInventories() {
        return inventoryRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public InventoryDto getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventaire introuvable"));
        return toDto(inventory);
    }

    public InventoryDto createInventory(CreateInventoryDto dto) {
        Game game = gameRepository.findById(dto.gameId())
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable"));

        Inventory inventory = new Inventory();
        inventory.setGame(game);
        inventory.setStock(dto.stock());

        return toDto(inventoryRepository.save(inventory));
    }

    public InventoryDto updateInventory(Long id, UpdateInventoryDto dto) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventaire introuvable"));

        Game game = gameRepository.findById(dto.gameId())
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable"));

        inventory.setStock(dto.stock());
        inventory.setGame(game);

        return toDto(inventoryRepository.save(inventory));
    }

    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventaire introuvable");
        }
        inventoryRepository.deleteById(id);
    }

    private InventoryDto toDto(Inventory inventory) {
        return new InventoryDto(
                inventory.getId(),
                inventory.getStock(),
                inventory.getGame().getId(),
                inventory.getGame().getName()
        );
    }
}