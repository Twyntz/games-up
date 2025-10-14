package com.gamesup.api.controller;

import com.gamesup.api.dto.LoginInputDto;
import com.gamesup.api.dto.LoginOutputDto;
import com.gamesup.api.dto.RegisterInputDto;
import com.gamesup.api.enumeration.Role;
import com.gamesup.api.model.User;
import com.gamesup.api.repository.UserRepository;
import com.gamesup.api.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.gamesup.api.exception.HttpConflictException;
import com.gamesup.api.response.ApiResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthenticationManager authManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginOutputDto>> register(@Valid @RequestBody RegisterInputDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new HttpConflictException("Cette email est déjà utilisé");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.CLIENT);
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        LoginOutputDto loginData = new LoginOutputDto(token, user.getEmail(), user.getRole().name());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Utilisateur bien inscrit et connecté", loginData));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginOutputDto>> login(@Valid @RequestBody LoginInputDto dto) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        LoginOutputDto loginData = new LoginOutputDto(token, user.getEmail(), user.getRole().name());

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Connection réussi", loginData));
    }
}