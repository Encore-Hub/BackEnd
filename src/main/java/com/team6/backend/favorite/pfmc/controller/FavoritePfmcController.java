package com.team6.backend.favorite.pfmc.controller;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.favorite.pfmc.dto.FavoritePfmcRequestDto;
import com.team6.backend.favorite.pfmc.dto.FavoritePfmcResponseDto;
import com.team6.backend.favorite.pfmc.service.FavoritePfmcService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorite-pfmc")
public class FavoritePfmcController {

    private final FavoritePfmcService favoritePfmcService;
    private final ModelMapper modelMapper;

    public FavoritePfmcController(FavoritePfmcService favoritePfmcService, ModelMapper modelMapper) {
        this.favoritePfmcService = favoritePfmcService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/toggle")
    public ResponseEntity<String> toggleFavoritePfmc(@RequestBody FavoritePfmcRequestDto request) {
        try {
            favoritePfmcService.toggleFavoritePfmc(request);
            return ResponseEntity.ok("즐겨찾기 PFMC 상태가 성공적으로 토글되었습니다.");
        } catch (EncoreHubException e) {
            // 사용자 정의 예외 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("오류: " + e.getMessage());
        } catch (Exception e) {
            // 일반 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("내부 서버 오류: " + e.getMessage());
        }
    }

    @GetMapping("/members/{memberId}/favorite-pfmc")
    public ResponseEntity<List<FavoritePfmcResponseDto>> getFavoritePfmcListByMemberId(@PathVariable Long memberId) {
        try {
            List<FavoritePfmcResponseDto> favoritePfmcList = favoritePfmcService.getFavoritePfmcListByMemberId(memberId)
                    .stream()
                    .map(fp -> modelMapper.map(fp, FavoritePfmcResponseDto.class))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(favoritePfmcList);
        } catch (EncoreHubException e) {
            // 사용자 정의 예외 처리
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList()); // 또는 애플리케이션 로직에 따라 다르게 처리할 수 있습니다.
        } catch (Exception e) {
            // 일반 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // 또는 애플리케이션 로직에 따라 다르게 처리할 수 있습니다.
        }
    }
}
