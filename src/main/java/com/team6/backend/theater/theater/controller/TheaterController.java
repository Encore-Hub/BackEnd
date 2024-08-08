package com.team6.backend.theater.theater.controller;


import com.team6.backend.theater.api.dto.TheaterDetailResponseDto;
import com.team6.backend.theater.theater.dto.RegionTheaterRequestDto;
import com.team6.backend.theater.theater.dto.RegionTheaterResponseDto;
import com.team6.backend.theater.theater.dto.SearchTheaterRequestDto;
import com.team6.backend.theater.theater.dto.SearchTheaterResponseDto;
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
        try {
            RegionTheaterRequestDto requestDto = new RegionTheaterRequestDto(sidonm, gugunnm);
            List<RegionTheaterResponseDto> responseList = theaterService.getRegionTheaterList(requestDto);
            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{mt10id}")
    public ResponseEntity<TheaterDetailResponseDto> getTheaterDetail(@PathVariable String mt10id) {
        try {
            TheaterDetailResponseDto responseDto = theaterService.getTheaterDetail(mt10id);
            if (responseDto != null) {
                return ResponseEntity.ok(responseDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
