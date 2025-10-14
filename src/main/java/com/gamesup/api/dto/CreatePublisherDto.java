package com.gamesup.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePublisherDto(@NotBlank String name) {}