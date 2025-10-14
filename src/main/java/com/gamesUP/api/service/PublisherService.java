package com.gamesup.api.service;

import com.gamesup.api.dto.PublisherDto;
import com.gamesup.api.dto.CreatePublisherDto;
import com.gamesup.api.dto.UpdatePublisherDto;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.Publisher;
import com.gamesup.api.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherDto> getAllPublishers() {
        return publisherRepository.findAll()
                .stream()
                .map(p -> new PublisherDto(p.getId(), p.getName()))
                .toList();
    }

    public PublisherDto getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable"));
        return new PublisherDto(publisher.getId(), publisher.getName());
    }

    public PublisherDto createPublisher(CreatePublisherDto dto) {
        Publisher publisher = new Publisher();
        publisher.setName(dto.name());
        Publisher saved = publisherRepository.save(publisher);
        return new PublisherDto(saved.getId(), saved.getName());
    }

    public PublisherDto updatePublisher(Long id, UpdatePublisherDto dto) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable"));
        publisher.setName(dto.name());
        Publisher updated = publisherRepository.save(publisher);
        return new PublisherDto(updated.getId(), updated.getName());
    }

    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Éditeur introuvable"));
        publisherRepository.deleteById(publisher.getId());
    }
}