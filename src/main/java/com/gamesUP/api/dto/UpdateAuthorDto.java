package com.gamesup.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAuthorDto(@NotBlank String name) {}