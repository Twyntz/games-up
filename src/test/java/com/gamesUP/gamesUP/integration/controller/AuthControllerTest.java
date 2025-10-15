package com.gamesup.api.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesup.api.config.WebMvcTestNoSecurity;
import com.gamesup.api.controller.AuthController;
import com.gamesup.api.dto.LoginInputDto;
import com.gamesup.api.dto.RegisterInputDto;
import com.gamesup.api.enumeration.Role;
import com.gamesup.api.model.User;
import com.gamesup.api.repository.UserRepository;
import com.gamesup.api.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

@WebMvcTestNoSecurity(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @Test
    void register_ShouldCreateNewUser() throws Exception {
        RegisterInputDto dto = new RegisterInputDto("john@example.com", "password", "John Doe");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);

        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        when(jwtService.generateToken(1L, "john@example.com", "CLIENT")).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value("john@example.com"))
                .andExpect(jsonPath("$.data.token").value("fake-jwt-token"));
    }

    @Test
    void register_ShouldReturnConflict_WhenEmailAlreadyUsed() throws Exception {
        RegisterInputDto dto = new RegisterInputDto("john@example.com", "password", "John Doe");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("Cette email est déjà utilisé")));
    }

    @Test
    void login_ShouldAuthenticateUser() throws Exception {
        LoginInputDto dto = new LoginInputDto("john@example.com", "password");

        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");
        user.setPassword("hashedPassword");
        user.setRole(Role.CLIENT);

        when(userRepository.findByEmail("john@example.com")).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken(1L, "john@example.com", "CLIENT")).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("john@example.com"))
                .andExpect(jsonPath("$.data.token").value("fake-jwt-token"));
    }

    @Test
    void login_ShouldFailWithInvalidCredentials() throws Exception {
        LoginInputDto dto = new LoginInputDto("john@example.com", "wrongPassword");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }
}