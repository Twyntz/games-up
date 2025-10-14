package com.gamesup.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAuthorDto(@NotBlank String name) {}