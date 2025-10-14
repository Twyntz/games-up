package com.gamesup.api.dto.inventory;

public record InventoryDto(Long id, int stock, Long gameId, String gameName) {}