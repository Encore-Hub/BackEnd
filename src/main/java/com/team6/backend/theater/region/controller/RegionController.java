package com.team6.backend.theater.region.controller;

import com.team6.backend.theater.region.dto.RegionRequestDto;
import com.team6.backend.theater.region.dto.RegionResponseDto;
import com.team6.backend.theater.region.service.RegionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegionController {
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping("/api/region")
    public ResponseEntity<RegionResponseDto> getRegionGugunnm(
            @RequestParam String sidonm) {
        RegionRequestDto regionRequestDto = new RegionRequestDto(sidonm);
        RegionResponseDto regionResponseDto = regionService.getRegionGugunnm(regionRequestDto);

        // Assuming a valid RegionResponseDto will be returned if no exceptions are thrown
        return ResponseEntity.ok(regionResponseDto);
    }
}
