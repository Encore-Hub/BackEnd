package com.team6.backend.favorite.theater.controller;

import com.team6.backend.favorite.theater.dto.FavoriteTheaterRequestDto;
import com.team6.backend.favorite.theater.dto.FavoriteTheaterResponseDto;
import com.team6.backend.favorite.theater.dto.FavoriteTheaterToggleResponseDto;
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
    public ResponseEntity<FavoriteTheaterToggleResponseDto> toggleFavoriteTheater(
            @RequestBody FavoriteTheaterRequestDto request,
            HttpServletRequest httpServletRequest) {

        String accessToken = jwtUtil.getAccessTokenFromHeader(httpServletRequest);
        if (accessToken == null) {
            return ResponseEntity.badRequest()
                    .body(new FavoriteTheaterToggleResponseDto("Invalid token.", false));
        }

        String email = jwtUtil.getEmailFromToken(accessToken);

        // Toggle favorite status and get the new status
        boolean isFavorited = favoriteTheaterService.toggleFavoriteTheater(request.getMt10id(), email);

        // Create response DTO
        FavoriteTheaterToggleResponseDto response = new FavoriteTheaterToggleResponseDto(
                "Favorite theater status toggled successfully.",
                isFavorited
        );

        return ResponseEntity.ok(response);
    }



    @GetMapping("/mypage")
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
                        theater.getFcltynm(),
                        theater.getMt10id(),
                        theater.isFavorited()

                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(favoriteTheaters);
    }
}
