package com.team6.backend.theater.api.controller;

import com.team6.backend.theater.api.service.TheaterDetailService;
import com.team6.backend.theater.api.service.TheaterListService;
import com.team6.backend.theater.api.service.TheaterPfmcDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openapi/theaters")
public class TheaterApiController {

    private final TheaterListService theaterListService;
    private final TheaterDetailService theaterDetailService;
    private final TheaterPfmcDetailService theaterPfmcDetailService;

    public TheaterApiController(TheaterListService theaterListService,
                                TheaterDetailService theaterDetailService,
                                TheaterPfmcDetailService theaterPfmcDetailService) {
        this.theaterListService = theaterListService;
        this.theaterDetailService = theaterDetailService;
        this.theaterPfmcDetailService = theaterPfmcDetailService;
    }

    @PostMapping("/save-list")
    public ResponseEntity<String> saveTheaterList() {
        theaterListService.fetchAndSaveTheaterList();
        return ResponseEntity.ok("Theater list saved successfully.");
    }

    @PostMapping("/save-details")
    public ResponseEntity<String> saveTheaterDetails() throws Exception {
        theaterDetailService.fetchAndSaveTheaterDetails();
        return ResponseEntity.ok("Theater details updated successfully.");
    }

    @PostMapping("/save-pfmc-details")
    public ResponseEntity<String> savePfmcDetails() throws Exception {
        theaterPfmcDetailService.fetchAndSaveTheaterPfmcDetails();
        return ResponseEntity.ok("Theater PFMC details updated successfully.");
    }
}
