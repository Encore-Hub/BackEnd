package com.team6.backend.favorite.pfmc.controller;

import com.team6.backend.favorite.pfmc.dto.FavoritePfmcRequestDto;
import com.team6.backend.favorite.pfmc.dto.FavoritePfmcResponseDto;
import com.team6.backend.favorite.pfmc.service.FavoritePfmcService;
import com.team6.backend.security.jwt.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorite-pfmc")
public class FavoritePfmcController {

    private final FavoritePfmcService favoritePfmcService;
    private final JwtUtil jwtUtil;

    public FavoritePfmcController(FavoritePfmcService favoritePfmcService, JwtUtil jwtUtil) {
        this.favoritePfmcService = favoritePfmcService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleFavoritePfmc(@RequestBody FavoritePfmcRequestDto request, HttpServletRequest httpServletRequest) {
        String accessToken = jwtUtil.getAccessTokenFromHeader(httpServletRequest);
        if (accessToken == null) {
            return ResponseEntity.badRequest().body("Invalid token.");
        }
        String email = jwtUtil.getEmailFromToken(accessToken);
        favoritePfmcService.toggleFavoritePfmc(request.getPerformanceId(), email);
        return ResponseEntity.ok("Favorite PFMC status toggled successfully.");
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoritePfmcResponseDto>> getFavoritePfmcList(HttpServletRequest httpServletRequest) {
        String accessToken = jwtUtil.getAccessTokenFromHeader(httpServletRequest);
        if (accessToken == null) {
            return ResponseEntity.badRequest().body(List.of()); // Or handle differently
        }
        String email = jwtUtil.getEmailFromToken(accessToken);
        List<FavoritePfmcResponseDto> favoritePfmcList = favoritePfmcService.getFavoritePfmcListByEmail(email);
        return ResponseEntity.ok(favoritePfmcList);
    }
}
