package com.gamesup.api.dto.reco;

import java.util.List;

public record RecommendationApiResponse(List<RecommendationItemDto> recommendations) {}