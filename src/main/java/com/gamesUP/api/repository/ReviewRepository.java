package com.gamesup.api.repository;

import com.gamesup.api.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByGameId(Long gameId);
    List<Review> findByUserId(Long userId);
    boolean existsByUserIdAndGameId(Long userId, Long gameId);
} 