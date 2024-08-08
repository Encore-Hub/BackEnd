package com.team6.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TheaterException extends RuntimeException {
    private ErrorCode errorCode;
}
