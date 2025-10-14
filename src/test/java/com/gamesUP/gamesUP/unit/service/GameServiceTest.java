package com.gamesup.api.unit.service;

import com.gamesup.api.dto.game.CreateGameDto;
import com.gamesup.api.dto.game.UpdateGameDto;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.*;
import com.gamesup.api.repository.*;
import com.gamesup.api.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private GameRepository gameRepository;
    private AuthorRepository authorRepository;
    private CategoryRepository categoryRepository;
    private PublisherRepository publisherRepository;
    private GameService gameService;

    private final Author author = new Author(1L, "Auteur", null);
    private final Category category = new Category(1L, "Categorie", null);
    private final Publisher publisher = new Publisher(1L, "Editeur", null);

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        authorRepository = mock(AuthorRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        publisherRepository = mock(PublisherRepository.class);
        gameService = new GameService(gameRepository, authorRepository, categoryRepository, publisherRepository);
    }

    @Test
    void getAllGames_shouldReturnList() {
        Game game = createSampleGame(1L);
        when(gameRepository.findAll()).thenReturn(List.of(game));

        var result = gameService.getAllGames();

        assertEquals(1, result.size());
        assertEquals("Game1", result.getFirst().name());
    }

    @Test
    void getGameById_shouldReturnGame() {
        Game game = createSampleGame(1L);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        var result = gameService.getGameById(1L);

        assertEquals("Game1", result.name());
    }

    @Test
    void getGameById_shouldThrowIfNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gameService.getGameById(1L));
    }

    @Test
    void createGame_shouldReturnCreatedGame() {
        CreateGameDto dto = new CreateGameDto("Game1", new BigDecimal("39.99"), LocalDate.now(), 1L, 1L, 1L);
        Game saved = createSampleGame(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(gameRepository.save(any(Game.class))).thenReturn(saved);

        var result = gameService.createGame(dto);

        assertEquals("Game1", result.name());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void updateGame_shouldReturnUpdatedGame() {
        Game existing = createSampleGame(1L);
        UpdateGameDto dto = new UpdateGameDto("Updated Game", new BigDecimal("49.99"), LocalDate.now(), 1L, 1L, 1L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(gameRepository.save(any(Game.class))).thenReturn(existing);

        var result = gameService.updateGame(1L, dto);

        assertEquals("Updated Game", result.name());
        assertEquals(new BigDecimal("49.99"), result.price());
    }

    @Test
    void deleteGame_shouldDelete() {
        Game game = createSampleGame(1L);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        gameService.deleteGame(1L);

        verify(gameRepository).deleteById(1L);
    }

    private Game createSampleGame(Long id) {
        Game g = new Game();
        g.setId(id);
        g.setName("Game1");
        g.setPrice(new BigDecimal("39.99"));
        g.setReleaseDate(LocalDate.now());
        g.setAuthor(author);
        g.setCategory(category);
        g.setPublisher(publisher);
        return g;
    }
}