package com.gamesup.api.dto.order;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateOrderDto(@NotBlank String shippingAddress, @NotBlank List<CreateOrderLineDto> lines) {}