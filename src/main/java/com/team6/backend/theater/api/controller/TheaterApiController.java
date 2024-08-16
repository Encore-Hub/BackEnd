package com.team6.backend.theater.api.controller;

import com.team6.backend.common.exception.ErrorCode;
import com.team6.backend.common.exception.TheaterException;
import com.team6.backend.theater.api.service.TheaterDetailService;
import com.team6.backend.theater.api.service.TheaterListService;
import com.team6.backend.theater.api.service.TheaterPfmcDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openapi/theaters")
public class TheaterApiController {

    private static final Logger logger = LoggerFactory.getLogger(TheaterApiController.class);


    private final TheaterListService theaterListService;
    private final TheaterDetailService theaterDetailService;
    private final TheaterPfmcDetailService theaterPfmcDetailService;

    @Autowired
    public TheaterApiController(
                                TheaterListService theaterListService,
                                TheaterDetailService theaterDetailService,
                                TheaterPfmcDetailService theaterPfmcDetailService) {

        this.theaterListService = theaterListService;
        this.theaterDetailService = theaterDetailService;
        this.theaterPfmcDetailService = theaterPfmcDetailService;
    }

    @PostMapping("/save-list")
    public ResponseEntity<String> saveTheaterList() {
        try {
            theaterListService.fetchAndSaveTheaterList();
            return ResponseEntity.ok("Theater list saved successfully.");
        } catch (Exception e) {
            logger.error("Error saving theater list", e);
            throw new TheaterException(ErrorCode.INVALID_THEATER_DATA);
        }
    }

    @PostMapping("/save-details")
    public ResponseEntity<String> saveTheaterDetails() {
        try {
            theaterDetailService.fetchAndSaveTheaterDetails();
            return ResponseEntity.ok("Theater details updated successfully.");
        } catch (Exception e) {
            logger.error("Error saving theater details", e);
            throw new TheaterException(ErrorCode.INVALID_THEATER_DATA);
        }
    }

    @PostMapping("/save-pfmc-details")
    public ResponseEntity<String> savePfmcDetails() {
        try {
            theaterPfmcDetailService.fetchAndSaveTheaterPfmcDetails();
            return ResponseEntity.ok("Theater PFMC details updated successfully.");
        } catch (Exception e) {
            logger.error("Error saving PFMC theater details", e);
            throw new TheaterException(ErrorCode.INVALID_THEATER_DATA);
        }
    }
}
