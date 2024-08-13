package com.team6.backend.favorite.theater.controller;

import com.team6.backend.favorite.theater.dto.FavoriteTheaterRequestDto;
import com.team6.backend.favorite.theater.dto.FavoriteTheaterResponseDto;
import com.team6.backend.favorite.theater.service.FavoriteTheaterService;
import com.team6.backend.security.jwt.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorite-theaters")
public class FavoriteTheaterController {

    private final FavoriteTheaterService favoriteTheaterService;
    private final JwtUtil jwtUtil;

    public FavoriteTheaterController(FavoriteTheaterService favoriteTheaterService, JwtUtil jwtUtil) {
        this.favoriteTheaterService = favoriteTheaterService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleFavoriteTheater(@RequestBody FavoriteTheaterRequestDto request, HttpServletRequest httpServletRequest) {
        String accessToken = jwtUtil.getAccessTokenFromHeader(httpServletRequest);
        if (accessToken == null) {
            return ResponseEntity.badRequest().body("Invalid token.");
        }
        String email = jwtUtil.getEmailFromToken(accessToken);
        favoriteTheaterService.toggleFavoriteTheater(request.getTheaterId(), email);

        return ResponseEntity.ok("Favorite theater status toggled successfully.");
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteTheaterResponseDto>> getFavoriteTheaterList(HttpServletRequest httpServletRequest) {


        String accessToken = jwtUtil.getAccessTokenFromHeader(httpServletRequest);
        if (accessToken == null) {
            return ResponseEntity.badRequest().body(List.of()); // Or handle differently
        }
        String email = jwtUtil.getEmailFromToken(accessToken);

        List<FavoriteTheaterResponseDto> favoriteTheaters = favoriteTheaterService.getAllFavoriteTheatersByEmail(email)
                .stream()
                .map(theater -> new FavoriteTheaterResponseDto(
                        theater.getId(),
                        theater.getTheaterName(),
                        theater.getTheaterId(),
                        theater.isFavorited()

                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(favoriteTheaters);
    }
}
