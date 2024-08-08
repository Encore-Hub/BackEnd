package com.team6.backend.favorite.theater.controller;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.favorite.theater.dto.FavoriteTheaterRequestDto;
import com.team6.backend.favorite.theater.dto.FavoriteTheaterResponseDto;
import com.team6.backend.favorite.theater.service.FavoriteTheaterService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorite-theaters")
public class FavoriteTheaterController {

    private final FavoriteTheaterService favoriteTheaterService;
    private final ModelMapper modelMapper;

    public FavoriteTheaterController(FavoriteTheaterService favoriteTheaterService, ModelMapper modelMapper) {
        this.favoriteTheaterService = favoriteTheaterService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleFavoriteTheater(@RequestBody FavoriteTheaterRequestDto request) {
        try {
            favoriteTheaterService.toggleFavoriteTheater(request);
            return ResponseEntity.ok("Favorite status toggled successfully.");
        } catch (EncoreHubException e) {
            // Custom exception handling
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            // General exception handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/members/{memberId}/favorite-theaters")
    public ResponseEntity<List<FavoriteTheaterResponseDto>> getAllFavoriteTheaters(@PathVariable Long memberId) {
        try {
            List<FavoriteTheaterResponseDto> favoriteTheaters = favoriteTheaterService.getAllFavoriteTheatersByMemberId(memberId)
                    .stream()
                    .map(theater -> modelMapper.map(theater, FavoriteTheaterResponseDto.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(favoriteTheaters);
        } catch (EncoreHubException e) {
            // Custom exception handling
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList()); // or handle differently based on your application logic
        } catch (Exception e) {
            // General exception handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // or handle differently based on your application logic
        }
    }

}
