package com.team6.backend.boxOffice.controller;

import com.team6.backend.boxOffice.dto.BoxOfficeDto;
import com.team6.backend.boxOffice.entity.BoxOffice;
import com.team6.backend.boxOffice.service.BoxOfficeApiService;
import com.team6.backend.boxOffice.service.BoxOfficeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/boxoffice")
public class BoxOfficeController {

    private static final Logger logger = LoggerFactory.getLogger(BoxOfficeController.class);

    private final BoxOfficeApiService boxOfficeApiService;
    private final BoxOfficeService boxOfficeService;

    @Autowired
    public BoxOfficeController(BoxOfficeApiService boxOfficeApiService, BoxOfficeService boxOfficeService) {
        this.boxOfficeApiService = boxOfficeApiService;
        this.boxOfficeService = boxOfficeService;
    }

    // Fetch and save box office data
    @PostMapping("/fetch")
    public ResponseEntity<String> fetchAndSaveBoxOfficeData() {
        try {
            boxOfficeApiService.fetchAndSaveBoxOfficeData();
            return ResponseEntity.status(HttpStatus.CREATED).body("Box office data fetched and saved successfully.");
        } catch (Exception e) {
            logger.error("Failed to fetch and save box office data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch and save box office data.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<BoxOfficeDto>> getAllBoxOfficeData() {
        try {
            List<BoxOfficeDto> boxOfficeData = boxOfficeService.getAllBoxOfficeData();
            return ResponseEntity.ok(boxOfficeData);
        } catch (Exception e) {
            logger.error("Failed to fetch box office data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
