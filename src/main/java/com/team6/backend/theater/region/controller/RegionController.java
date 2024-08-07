package com.team6.backend.theater.region.controller;



import com.team6.backend.common.exception.TheaterException;
import com.team6.backend.theater.region.dto.RegionRequestDto;
import com.team6.backend.theater.region.dto.RegionResponseDto;
import com.team6.backend.theater.region.service.RegionService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/region")
    public ResponseEntity<RegionResponseDto> getRegionGugunnm(
            @RequestParam String sidonm) {
        try {
            RegionRequestDto regionRequestDto = new RegionRequestDto(sidonm);
            RegionResponseDto regionResponseDto = regionService.getRegionGugunnm(regionRequestDto);
            return ResponseEntity.ok(regionResponseDto);
        } catch (TheaterException e) {
            // Return 400 Bad Request if the exception is thrown
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegionResponseDto(e.getErrorCode().getMessage(), null));
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
