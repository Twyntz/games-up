package com.gamesup.api.service;

import com.gamesup.api.dto.reco.*;
import com.gamesup.api.dto.review.ReviewDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final ReviewService reviewService;

    public RecommendationService(
            RestTemplate restTemplate,
            @Value("${recommendations.python.base-url:http://localhost:5000}") String baseUrl, ReviewService reviewService
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.reviewService = reviewService;
    }

    public List<RecommendationItemDto> getRecommendationsForUser(Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUserId(userId);

        List<Map<String, Object>> purchases = reviews.stream()
                .map(r -> Map.<String, Object>of(
                        "game_id", r.gameId(),
                        "rating", r.rating()
                ))
                .toList();

        Map<String, Object> payload = new HashMap<>();
        payload.put("user_id", userId);
        payload.put("purchases", purchases);

        ResponseEntity<RecommendationApiResponse> response =
                restTemplate.postForEntity(
                        baseUrl + "/recommendations",
                        payload,
                        RecommendationApiResponse.class
                );

        RecommendationApiResponse body = response.getBody();
        return (body == null) ? List.of() : body.recommendations();
    }
}