package com.gamesup.api.unit.service;

import com.gamesup.api.dto.wishlist.WishlistDto;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.Game;
import com.gamesup.api.model.User;
import com.gamesup.api.model.Wishlist;
import com.gamesup.api.repository.GameRepository;
import com.gamesup.api.repository.UserRepository;
import com.gamesup.api.repository.WishlistRepository;
import com.gamesup.api.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

    private WishlistRepository wishlistRepository;
    private GameRepository gameRepository;
    private UserRepository userRepository;
    private WishlistService wishlistService;

    private final User user = new User(1L);
    private final Game game = new Game();
    private final Wishlist wishlist = new Wishlist(1L, user, game);

    @BeforeEach
    void setUp() {
        wishlistRepository = mock(WishlistRepository.class);
        gameRepository = mock(GameRepository.class);
        userRepository = mock(UserRepository.class);
        wishlistService = new WishlistService(wishlistRepository, gameRepository, userRepository);

        game.setId(1L);
        game.setName("Game1");
    }

    @Test
    void getWishlist_shouldReturnListOfDtos() {
        when(wishlistRepository.findByUserId(1L)).thenReturn(List.of(wishlist));

        List<WishlistDto> result = wishlistService.getWishlist(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().userId());
        assertEquals("Game1", result.getFirst().gameName());
    }

    @Test
    void addToWishlist_shouldReturnDto() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(wishlistRepository.findByUserIdAndGameId(1L, 1L)).thenReturn(Optional.empty());
        when(wishlistRepository.save(any(Wishlist.class))).thenAnswer(invocation -> {
            Wishlist w = invocation.getArgument(0);
            w.setId(10L);
            return w;
        });

        WishlistDto result = wishlistService.addToWishlist(1L, 1L);

        assertEquals(10L, result.id());
        assertEquals(1L, result.userId());
        assertEquals(1L, result.gameId());
        assertEquals("Game1", result.gameName());
    }

    @Test
    void addToWishlist_shouldThrowIfGameNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> wishlistService.addToWishlist(1L, 1L));
    }

    @Test
    void addToWishlist_shouldThrowIfUserNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(wishlistRepository.findByUserIdAndGameId(1L, 1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> wishlistService.addToWishlist(1L, 1L));
    }

    @Test
    void addToWishlist_shouldThrowIfAlreadyExists() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(wishlistRepository.findByUserIdAndGameId(1L, 1L)).thenReturn(Optional.of(wishlist));

        assertThrows(IllegalStateException.class, () -> wishlistService.addToWishlist(1L, 1L));
    }

    @Test
    void removeFromWishlist_shouldCallDelete() {
        when(wishlistRepository.findByUserIdAndGameId(1L, 1L)).thenReturn(Optional.of(wishlist));

        wishlistService.removeFromWishlist(1L, 1L);

        verify(wishlistRepository).delete(wishlist);
    }

    @Test
    void removeFromWishlist_shouldThrowIfNotFound() {
        when(wishlistRepository.findByUserIdAndGameId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> wishlistService.removeFromWishlist(1L, 1L));
    }
}