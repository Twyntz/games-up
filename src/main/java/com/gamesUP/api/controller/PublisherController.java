package com.gamesup.api.controller;

import com.gamesup.api.dto.PublisherDto;
import com.gamesup.api.response.ApiResponse;
import com.gamesup.api.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PublisherDto>>> getAllPublishers() {
        List<PublisherDto> publishers = publisherService.getAllPublishers();
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Éditeurs récupérés avec succès", publishers)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PublisherDto>> getPublisherById(@PathVariable Long id) {
        PublisherDto publisher = publisherService.getPublisherById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Éditeur récupéré avec succès", publisher)
        );
    }
}