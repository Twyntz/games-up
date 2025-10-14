package com.gamesup.api.controller;

import com.gamesup.api.dto.game.*;
import com.gamesup.api.response.ApiResponse;
import com.gamesup.api.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/games")
public class GameAdminController {

    private final GameService gameService;

    public GameAdminController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GameDto>> createGame(@RequestBody CreateGameDto dto) {
        return ResponseEntity.status(201).body(
                new ApiResponse<>(201, "Jeu créé avec succès", gameService.createGame(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GameDto>> updateGame(@PathVariable Long id, @RequestBody UpdateGameDto dto) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Jeu mis à jour avec succès", gameService.updateGame(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Jeu supprimé avec succès", null));
    }
}