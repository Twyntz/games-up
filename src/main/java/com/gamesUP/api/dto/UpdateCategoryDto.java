package com.gamesup.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryDto(@NotBlank String name) {}