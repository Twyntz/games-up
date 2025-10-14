package com.gamesup.api.controller;

import com.gamesup.api.dto.wishlist.WishlistDto;
import com.gamesup.api.response.ApiResponse;
import com.gamesup.api.security.UserFromApi;
import com.gamesup.api.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WishlistDto>>> getWishlist(@AuthenticationPrincipal UserFromApi user) {
        List<WishlistDto> wishlist = wishlistService.getWishlist(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(200, "Wishlist fetched successfully", wishlist));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WishlistDto>> addToWishlist(
            @AuthenticationPrincipal UserFromApi user,
            @RequestParam Long gameId
    ) {
        WishlistDto created = wishlistService.addToWishlist(user.getId(), gameId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Game added to wishlist successfully", created));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> removeFromWishlist(
            @AuthenticationPrincipal UserFromApi user,
            @RequestParam Long gameId
    ) {
        wishlistService.removeFromWishlist(user.getId(), gameId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Game removed from wishlist successfully"));
    }
}