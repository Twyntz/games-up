package com.gamesup.api.response;

public record ApiResponse<T>(int status, String message, T data) {
    public ApiResponse(int status, String message) {
        this(status, message, null);
    }
}