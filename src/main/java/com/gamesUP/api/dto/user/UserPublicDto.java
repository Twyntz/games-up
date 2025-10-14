package com.gamesup.api.dto.user;

import java.util.List;

public record UserPublicDto(Long id, String username, List<String> purchasedGames) {}