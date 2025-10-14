package com.gamesup.api.service;

import com.gamesup.api.dto.user.UserPublicDto;
import com.gamesup.api.exception.ResourceNotFoundException;
import com.gamesup.api.model.User;
import com.gamesup.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserPublicDto getUserById(Long id) {
        User user = userRepository.findByIdWithOrders(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        List<String> purchasedGames = user.getOrders().stream()
                .flatMap(order -> order.getLines().stream())
                .map(orderLine -> orderLine.getGame().getName())
                .distinct()
                .toList();

        return new UserPublicDto(user.getId(), user.getName(), purchasedGames);
    }
}