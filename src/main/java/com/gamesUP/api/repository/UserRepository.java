package com.gamesup.api.repository;

import com.gamesup.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
                SELECT u FROM users u
                LEFT JOIN FETCH u.orders o
                LEFT JOIN FETCH o.lines ol
                LEFT JOIN FETCH ol.game
                WHERE u.id = :id
            """)
    Optional<User> findByIdWithOrders(@Param("id") Long id);
}