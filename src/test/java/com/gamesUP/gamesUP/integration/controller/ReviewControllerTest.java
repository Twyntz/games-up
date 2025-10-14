package com.gamesup.api.integration.controller;

import com.gamesup.api.config.WebMvcTestNoSecurity;
import com.gamesup.api.controller.ReviewController;
import com.gamesup.api.dto.review.ReviewDto;
import com.gamesup.api.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTestNoSecurity(controllers = ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private final ReviewDto sampleReview = new ReviewDto(
            1L,
            4,
            "Nice game",
            "user1",
            1L,
            "Game1"
    );

    @Test
    void getReviewsByGame_shouldReturnList() throws Exception {
        when(reviewService.getReviewsByGameId(10L)).thenReturn(List.of(sampleReview));

        mockMvc.perform(get("/reviews/game/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].message").value("Nice game"))
                .andExpect(jsonPath("$.data[0].gameTitle").value("Game1"));
    }
}