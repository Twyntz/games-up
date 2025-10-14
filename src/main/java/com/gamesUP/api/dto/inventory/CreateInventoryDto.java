package com.gamesup.api.dto.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateInventoryDto(@NotNull Long gameId, @Min(0) int stock) {}