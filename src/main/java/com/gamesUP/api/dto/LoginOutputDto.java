package com.gamesup.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginOutputDto {
    private String token;
    private String email;
    private String role;
}