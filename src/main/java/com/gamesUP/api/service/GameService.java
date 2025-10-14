package com.gamesup.api.service;

import com.gamesup.api.dto.game.*;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.*;
import com.gamesup.api.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;

    public GameService(GameRepository gameRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository, PublisherRepository publisherRepository) {
        this.gameRepository = gameRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<GameDto> getAllGames() {
        return gameRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public GameDto getGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable"));
        return toDto(game);
    }

    public GameDto createGame(CreateGameDto dto) {
        Game game = new Game();
        game.setName(dto.name());
        game.setPrice(dto.price());
        game.setReleaseDate(dto.releaseDate());
        game.setAuthor(authorRepository.findById(dto.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable")));
        game.setCategory(categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable")));
        game.setPublisher(publisherRepository.findById(dto.publisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable")));

        Game saved = gameRepository.save(game);
        return toDto(saved);
    }

    public GameDto updateGame(Long id, UpdateGameDto dto) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable"));

        game.setName(dto.name());
        game.setPrice(dto.price());
        game.setReleaseDate(dto.releaseDate());
        game.setAuthor(authorRepository.findById(dto.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Auteur introuvable")));
        game.setCategory(categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable")));
        game.setPublisher(publisherRepository.findById(dto.publisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable")));

        return toDto(gameRepository.save(game));
    }

    public void deleteGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jeu introuvable"));
        gameRepository.deleteById(game.getId());
    }

    private GameDto toDto(Game g) {
        return new GameDto(
                g.getId(),
                g.getName(),
                g.getPrice(),
                g.getReleaseDate(),
                g.getAuthor().getName(),
                g.getCategory().getName(),
                g.getPublisher().getName()
        );
    }
}