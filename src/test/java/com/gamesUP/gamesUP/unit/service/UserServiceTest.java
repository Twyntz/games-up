package com.gamesup.api.unit.service;

import com.gamesup.api.dto.user.UserPublicDto;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.*;
import com.gamesup.api.repository.UserRepository;
import com.gamesup.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void getUserById_shouldReturnUserPublicDto_whenUserExists() {
        Game game1 = new Game();
        game1.setName("Game1");

        Game game2 = new Game();
        game2.setName("Game2");

        OrderLine line1 = new OrderLine();
        line1.setGame(game1);

        OrderLine line2 = new OrderLine();
        line2.setGame(game2);

        Order order = new Order();
        order.setLines(Set.of(line1, line2));

        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setOrders(Set.of(order));

        when(userRepository.findByIdWithOrders(1L)).thenReturn(Optional.of(user));

        UserPublicDto result = userService.getUserById(1L);

        assertEquals(1L, result.id());
        assertEquals("John", result.username());
        assertEquals(2, result.purchasedGames().size());
        assertTrue(result.purchasedGames().contains("Game1"));
        assertTrue(result.purchasedGames().contains("Game2"));
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByIdWithOrders(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
    }
}