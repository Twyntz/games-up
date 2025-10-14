package com.gamesup.api.unit.service;

import com.gamesup.api.dto.review.CreateReviewDto;
import com.gamesup.api.dto.review.ReviewDto;
import com.gamesup.api.enumeration.Role;
import com.gamesup.api.model.Game;
import com.gamesup.api.model.Review;
import com.gamesup.api.model.User;
import com.gamesup.api.repository.GameRepository;
import com.gamesup.api.repository.ReviewRepository;
import com.gamesup.api.repository.UserRepository;
import com.gamesup.api.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private GameRepository gameRepository;
    private ReviewService reviewService;

    private final User sampleUser = new User();
    private final Game sampleGame = new Game();
    private final Review sampleReview = new Review();

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        userRepository = mock(UserRepository.class);
        gameRepository = mock(GameRepository.class);
        reviewService = new ReviewService(reviewRepository, userRepository, gameRepository);

        sampleUser.setId(1L);
        sampleUser.setName("John");
        sampleUser.setRole(Role.CLIENT);

        sampleGame.setId(2L);
        sampleGame.setName("Game1");

        sampleReview.setId(100L);
        sampleReview.setRating(4);
        sampleReview.setMessage("Great game!");
        sampleReview.setUser(sampleUser);
        sampleReview.setGame(sampleGame);
    }

    @Test
    void createReview_shouldCreateAndReturnDto() {
        CreateReviewDto dto = new CreateReviewDto(4, "Great game!", 2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(gameRepository.findById(2L)).thenReturn(Optional.of(sampleGame));
        when(reviewRepository.existsByUserIdAndGameId(1L, 2L)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(sampleReview);

        ReviewDto result = reviewService.createReview(1L, dto);

        assertEquals(4, result.rating());
        assertEquals("Great game!", result.message());
        assertEquals("John", result.username());
    }

    @Test
    void createReview_shouldThrowIfRatingInvalid() {
        CreateReviewDto dto = new CreateReviewDto(0, "Too low", 2L);
        assertThrows(IllegalArgumentException.class, () -> reviewService.createReview(1L, dto));
    }

    @Test
    void createReview_shouldThrowIfAlreadyExists() {
        CreateReviewDto dto = new CreateReviewDto(5, "Nice", 2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(gameRepository.findById(2L)).thenReturn(Optional.of(sampleGame));
        when(reviewRepository.existsByUserIdAndGameId(1L, 2L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> reviewService.createReview(1L, dto));
    }

    @Test
    void getReviewsByGameId_shouldReturnList() {
        when(reviewRepository.findByGameId(2L)).thenReturn(List.of(sampleReview));

        List<ReviewDto> results = reviewService.getReviewsByGameId(2L);

        assertEquals(1, results.size());
        assertEquals("Game1", results.getFirst().gameTitle());
    }

    @Test
    void getReviewsByUserId_shouldReturnList() {
        when(reviewRepository.findByUserId(1L)).thenReturn(List.of(sampleReview));

        List<ReviewDto> results = reviewService.getReviewsByUserId(1L);

        assertEquals(1, results.size());
        assertEquals("John", results.getFirst().username());
    }

    @Test
    void deleteReview_shouldDeleteIfOwner() {
        when(reviewRepository.findById(100L)).thenReturn(Optional.of(sampleReview));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        reviewService.deleteReview(100L, 1L);

        verify(reviewRepository).delete(sampleReview);
    }

    @Test
    void deleteReview_shouldThrowIfUnauthorized() {
        User otherUser = new User();
        otherUser.setId(99L);
        otherUser.setName("Hacker");
        otherUser.setRole(Role.CLIENT);

        sampleReview.setUser(otherUser);

        when(reviewRepository.findById(100L)).thenReturn(Optional.of(sampleReview));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        assertThrows(SecurityException.class, () -> reviewService.deleteReview(100L, 1L));
    }

    @Test
    void deleteReview_shouldAllowIfAdmin() {
        User reviewOwner = new User();
        reviewOwner.setId(99L);
        reviewOwner.setRole(Role.CLIENT);
        sampleReview.setUser(reviewOwner);

        User adminUser = new User();
        adminUser.setId(42L);
        adminUser.setRole(Role.ADMIN);

        when(reviewRepository.findById(100L)).thenReturn(Optional.of(sampleReview));
        when(userRepository.findById(42L)).thenReturn(Optional.of(adminUser));

        assertDoesNotThrow(() -> reviewService.deleteReview(100L, 42L));

        verify(reviewRepository).delete(sampleReview);
    }
}