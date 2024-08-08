package com.team6.backend.common.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TheaterException extends RuntimeException {
    private ErrorCode errorCode;

    public TheaterException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // ErrorCode의 메시지를 예외 메시지로 설정
        this.errorCode = errorCode;
    }
}
