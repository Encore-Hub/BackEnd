package com.team6.backend.favorite.pfmc.controller;

import com.team6.backend.favorite.pfmc.dto.FavoritePfmcRequestDto;
import com.team6.backend.favorite.pfmc.dto.FavoritePfmcResponseDto;
import com.team6.backend.favorite.pfmc.dto.FavoritePfmcToggleResponseDto;
import com.team6.backend.favorite.pfmc.service.FavoritePfmcService;
import com.team6.backend.security.jwt.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;


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
    public ResponseEntity<FavoritePfmcToggleResponseDto> toggleFavoritePfmc(
            @RequestBody FavoritePfmcRequestDto request,
            HttpServletRequest httpServletRequest) {

        String accessToken = jwtUtil.getAccessTokenFromHeader(httpServletRequest);
        if (accessToken == null) {
            return ResponseEntity.badRequest().body(new FavoritePfmcToggleResponseDto("Invalid token.", false));
        }

        String email = jwtUtil.getEmailFromToken(accessToken);

        // Toggle favorite status and get the new status
        boolean isFavorited = favoritePfmcService.toggleFavoritePfmc(request.getMt20id(), email);

        // Create response DTO
        FavoritePfmcToggleResponseDto response = new FavoritePfmcToggleResponseDto("Favorite PFMC status toggled successfully.", isFavorited);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/mypage")
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
