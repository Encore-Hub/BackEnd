package com.team6.backend.theater.api.controller;

import com.team6.backend.common.exception.ErrorCode;
import com.team6.backend.common.exception.TheaterException;
import com.team6.backend.theater.api.service.TheaterDetailService;
import com.team6.backend.theater.api.service.TheaterListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/theater")
public class TheaterApiController {

    private static final Logger logger = LoggerFactory.getLogger(TheaterApiController.class);

    private final JobLauncher jobLauncher;
    private final Job myJob;
    private final TheaterListService theaterListService;
    private final TheaterDetailService theaterDetailService;

    @Autowired
    public TheaterApiController(JobLauncher jobLauncher, Job myJob,
                                TheaterListService theaterListService,
                                TheaterDetailService theaterDetailService) {
        this.jobLauncher = jobLauncher;
        this.myJob = myJob;
        this.theaterListService = theaterListService;
        this.theaterDetailService = theaterDetailService;
    }

    @PutMapping("/update-all")
    public ResponseEntity<String> updateAllTheaterData() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        try {
            logger.debug("Starting batch job with parameters: {}", jobParameters);
            JobExecution jobExecution = jobLauncher.run(myJob, jobParameters);
            logger.debug("Batch job started with status: {}", jobExecution.getStatus());
            return ResponseEntity.ok("Batch job started with status: " + jobExecution.getStatus());
        } catch (Exception e) {
            logger.error("Error starting batch job", e);
            throw new TheaterException(ErrorCode.INVALID_THEATER_DATA);
        }
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
}
