package com.gamesup.api.controller;

import com.gamesup.api.dto.review.CreateReviewDto;
import com.gamesup.api.dto.review.ReviewDto;
import com.gamesup.api.response.ApiResponse;
import com.gamesup.api.security.UserFromApi;
import com.gamesup.api.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(
            @RequestBody CreateReviewDto dto,
            @AuthenticationPrincipal UserFromApi user
    ) {
        ReviewDto review = reviewService.createReview(user.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Avis créé avec succès", review));
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getReviewsByGame(
            @PathVariable Long gameId
    ) {
        List<ReviewDto> reviews = reviewService.getReviewsByGameId(gameId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Avis récupérés avec succès", reviews));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getReviewsByUser(
            @AuthenticationPrincipal UserFromApi user
    ) {
        List<ReviewDto> reviews = reviewService.getReviewsByUserId(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(200, "Avis des utilisateurs récupérés avec succès", reviews));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserFromApi user
    ) {
        reviewService.deleteReview(id, user.getId());
        return ResponseEntity.ok(new ApiResponse<>(200, "Suppression de l'avis réussie", null));
    }
}