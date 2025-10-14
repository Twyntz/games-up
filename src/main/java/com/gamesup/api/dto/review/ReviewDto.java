package com.gamesup.api.dto.review;

public record ReviewDto(Long id, int rating, String message, String username, Long gameId, String gameTitle) {}