package com.gamesup.api.dto.game;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GameDto(Long id, String name, BigDecimal price, LocalDate releaseDate, String authorName, String categoryName, String publisherName) {}