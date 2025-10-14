package com.gamesup.api.controller;

import com.gamesup.api.dto.inventory.CreateInventoryDto;
import com.gamesup.api.dto.inventory.InventoryDto;
import com.gamesup.api.dto.inventory.UpdateInventoryDto;
import com.gamesup.api.response.ApiResponse;
import com.gamesup.api.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/inventories")
public class InventoryAdminController {

    private final InventoryService inventoryService;

    public InventoryAdminController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryDto>>> getAll() {
        List<InventoryDto> inventories = inventoryService.getAllInventories();
        return ResponseEntity.ok(new ApiResponse<>(200, "Inventaires récupérés avec succès", inventories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryDto>> getById(@PathVariable Long id) {
        InventoryDto dto = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Inventaire récupéré avec succès", dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryDto>> create(@RequestBody @Valid CreateInventoryDto dto) {
        InventoryDto created = inventoryService.createInventory(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Inventaire créé avec succès", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryDto>> update(@PathVariable Long id, @RequestBody @Valid UpdateInventoryDto dto) {
        InventoryDto updated = inventoryService.updateInventory(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(200, "Inventaire mis à jour avec succès", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Inventaire supprimé avec succès"));
    }
}