package com.gamesup.api.unit.service;

import com.gamesup.api.dto.AuthorDto;
import com.gamesup.api.dto.CreateAuthorDto;
import com.gamesup.api.dto.UpdateAuthorDto;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.Author;
import com.gamesup.api.repository.AuthorRepository;
import com.gamesup.api.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    private AuthorRepository authorRepository;
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorRepository = mock(AuthorRepository.class);
        authorService = new AuthorService(authorRepository);
    }

    @Test
    void getAllAuthors_shouldReturnList() {
        when(authorRepository.findAll()).thenReturn(Arrays.asList(
                new Author(1L, "Author1", null),
                new Author(2L, "Author2", null)
        ));

        List<AuthorDto> result = authorService.getAllAuthors();

        assertEquals(2, result.size());
        assertEquals("Author1", result.getFirst().name());
    }

    @Test
    void getAuthorById_shouldReturnAuthor() {
        Author author = new Author(1L, "Author1", null);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorDto result = authorService.getAuthorById(1L);

        assertEquals("Author1", result.name());
    }

    @Test
    void getAuthorById_shouldThrowIfNotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthorById(1L));
    }

    @Test
    void createAuthor_shouldSaveAndReturnDto() {
        CreateAuthorDto dto = new CreateAuthorDto("New Author");
        Author saved = new Author(1L, "New Author", null);

        when(authorRepository.save(any(Author.class))).thenReturn(saved);

        AuthorDto result = authorService.createAuthor(dto);

        assertEquals("New Author", result.name());
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void updateAuthor_shouldUpdateName() {
        Author existing = new Author(1L, "Old Name", null);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(authorRepository.save(any(Author.class))).thenReturn(existing);

        UpdateAuthorDto dto = new UpdateAuthorDto("Updated Name");
        AuthorDto result = authorService.updateAuthor(1L, dto);

        assertEquals("Updated Name", result.name());
    }

    @Test
    void deleteAuthor_shouldCallDelete() {
        Author existing = new Author(1L, "To Delete", null);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(existing));

        authorService.deleteAuthor(1L);

        verify(authorRepository).deleteById(1L);
    }
}