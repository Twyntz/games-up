package com.gamesup.api.dto.review;

public record CreateReviewDto (int rating, String message, Long gameId) {}