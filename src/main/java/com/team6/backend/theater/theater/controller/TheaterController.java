package com.team6.backend.theater.theater.controller;

import com.team6.backend.theater.theater.dto.*;
import com.team6.backend.theater.theater.service.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {
    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping("/search")
    public ResponseEntity<SearchTheaterResponseDto> searchTheater(@RequestParam String theaterName) {
        SearchTheaterRequestDto requestDto = new SearchTheaterRequestDto(theaterName);
        SearchTheaterResponseDto response = theaterService.searchTheater(requestDto);

        // 폐관 메시지에 따라 적절한 응답 반환
        if (response.getTheaters().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/region")
    public ResponseEntity<List<RegionTheaterResponseDto>> getRegionTheaterList(
            @RequestParam String sidonm,
            @RequestParam String gugunnm) {
        List<RegionTheaterResponseDto> responseList = theaterService.getRegionTheaterList(
                new RegionTheaterRequestDto(sidonm, gugunnm)
        );
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{mt10id}")
    public ResponseEntity<TheaterDetailPfmcResponseDto> getTheaterDetail(@PathVariable String mt10id) {
        TheaterDetailPfmcResponseDto responseDto = theaterService.getTheaterDetailWithPerformances(mt10id);
        return ResponseEntity.ok(responseDto);
    }
}