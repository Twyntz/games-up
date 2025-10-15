package com.gamesup.api.controller;

import com.gamesup.api.dto.reco.RecommendationItemDto;
import com.gamesup.api.response.ApiResponse;
import com.gamesup.api.security.UserFromApi;
import com.gamesup.api.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<RecommendationItemDto>>> getMyRecommendations(
            @AuthenticationPrincipal UserFromApi user
    ) {
        List<RecommendationItemDto> recommendations = recommendationService.getRecommendationsForUser(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(200, "Recommendations fetched successfully", recommendations));
    }
}