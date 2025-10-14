package com.gamesup.api.dto.game;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateGameDto(@NotBlank String name, @NotBlank BigDecimal price, @NotBlank LocalDate releaseDate, Long authorId, Long categoryId, Long publisherId) {}