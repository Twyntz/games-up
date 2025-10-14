package com.gamesup.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInputDto {

    @NotBlank
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @NotBlank
    private String name;
}