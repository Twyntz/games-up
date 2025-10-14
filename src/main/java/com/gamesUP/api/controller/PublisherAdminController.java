package com.gamesup.api.controller;

import com.gamesup.api.dto.PublisherDto;
import com.gamesup.api.dto.CreatePublisherDto;
import com.gamesup.api.dto.UpdatePublisherDto;
import com.gamesup.api.response.ApiResponse;
import com.gamesup.api.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/publishers")
public class PublisherAdminController {

    private final PublisherService publisherService;

    public PublisherAdminController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PublisherDto>> createPublisher(@Valid @RequestBody CreatePublisherDto dto) {
        PublisherDto created = publisherService.createPublisher(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Éditeur créé avec succès", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PublisherDto>> updatePublisher(@PathVariable Long id, @Valid @RequestBody UpdatePublisherDto dto) {
        PublisherDto updated = publisherService.updatePublisher(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Mise à jour de l'éditeur réussie", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Éditeur supprimé avec succès"));
    }
}