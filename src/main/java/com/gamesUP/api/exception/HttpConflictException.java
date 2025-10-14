package com.gamesup.api.exception;

public class HttpConflictException extends RuntimeException {
    public HttpConflictException(String message) {
        super(message);
    }
}