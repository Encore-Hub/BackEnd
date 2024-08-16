package com.team6.backend.common.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EncoreHubException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleEncoreHubException(EncoreHubException e) {
        logger.error("EncoreHubException occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException e) {
        logger.error("JWT token expired: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token has expired.");
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleSecurityException(SecurityException e) {
        logger.error("Security exception occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Security exception occurred.");
    }

    @ExceptionHandler(TheaterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleTheaterException(TheaterException e) {
        logger.error("TheaterException occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Theater error: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unexpected error occurred: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}
