package com.gamesup.api.dto.order;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderLineDto(@NotBlank Long gameId) {}