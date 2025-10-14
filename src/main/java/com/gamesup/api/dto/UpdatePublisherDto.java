package com.gamesup.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePublisherDto(@NotBlank String name) {}