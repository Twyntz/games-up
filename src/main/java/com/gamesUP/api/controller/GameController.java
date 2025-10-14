package com.gamesup.api.controller;

import com.gamesup.api.dto.game.GameDto;
import com.gamesup.api.response.ApiResponse;
import com.gamesup.api.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GameDto>>> getAllGames() {
        List<GameDto> games = gameService.getAllGames();
        return ResponseEntity.ok(new ApiResponse<>(200, "Jeux récupérés avec succès", games));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GameDto>> getGameById(@PathVariable Long id) {
        GameDto game = gameService.getGameById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Jeu récupéré avec succès", game));
    }
}