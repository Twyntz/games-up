package com.gamesup.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryDto(@NotBlank String name) {}