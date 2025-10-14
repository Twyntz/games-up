package com.gamesup.api.service;

import com.gamesup.api.dto.review.CreateReviewDto;
import com.gamesup.api.dto.review.ReviewDto;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.Game;
import com.gamesup.api.model.Review;
import com.gamesup.api.model.User;
import com.gamesup.api.repository.GameRepository;
import com.gamesup.api.repository.ReviewRepository;
import com.gamesup.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            GameRepository gameRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public ReviewDto createReview(Long userId, CreateReviewDto dto) {
        if (dto.rating() < 1 || dto.rating() > 5) {
            throw new IllegalArgumentException("La note doit être comprise entre 1 et 5.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        Game game = gameRepository.findById(dto.gameId())
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable"));

        boolean alreadyExists = reviewRepository.existsByUserIdAndGameId(userId, dto.gameId());
        if (alreadyExists) {
            throw new IllegalStateException("L'utilisateur a déjà évalué ce jeu.");
        }

        Review review = new Review();
        review.setRating(dto.rating());
        review.setMessage(dto.message());
        review.setUser(user);
        review.setGame(game);

        Review saved = reviewRepository.save(review);
        return mapToDto(saved);
    }

    public List<ReviewDto> getReviewsByGameId(Long gameId) {
        return reviewRepository.findByGameId(gameId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public List<ReviewDto> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public void deleteReview(Long reviewId, Long requesterId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Avis introuvable"));

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        if (!review.getUser().getId().equals(requesterId) && !requester.isAdmin()) {
            throw new SecurityException("Non autorisé à supprimer cet avis");
        }

        reviewRepository.delete(review);
    }


    private ReviewDto mapToDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getRating(),
                review.getMessage(),
                review.getUser().getName(),
                review.getGame().getId(),
                review.getGame().getName()
        );
    }
}