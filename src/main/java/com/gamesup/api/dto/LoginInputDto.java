package com.gamesup.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginInputDto {
    @NotBlank
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank
    private String password;
}