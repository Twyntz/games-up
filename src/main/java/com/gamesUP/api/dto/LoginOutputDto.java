package com.gamesup.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginOutputDto {
    private String token;
    private String email;
    private String role;
}