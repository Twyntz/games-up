package com.gamesup.api.controller;

import com.gamesup.api.dto.user.UserPublicDto;
import com.gamesup.api.response.ApiResponse;
import com.gamesup.api.security.UserFromApi;
import com.gamesup.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserPublicDto>> getCurrentUser(@AuthenticationPrincipal UserFromApi user) {
        UserPublicDto userDto = userService.getUserById(user.getId());
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "L'utilisateur a été récupéré avec succès.", userDto)
        );
    }
}