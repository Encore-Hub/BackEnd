package com.team6.backend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USERID(HttpStatus.BAD_REQUEST, "MEMBER_001", "사용 중인 아이디입니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER_002", "사용 중인 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "MEMBER_003", "사용 중인 닉네임입니다."),
    DUPLICATED_PHONENUMBER(HttpStatus.BAD_REQUEST, "MEMBER_004", "사용 중인 전화번호입니다."),
    DUPLICATED_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER_005", "비밀번호를 다시 작성해주세요."),
    NOT_VALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER_006", "비밀번호를 다시 확인해주세요."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "MEMBER_007", "찾을 수 없는 회원입니다."),

    MISMATCH_REFRESH_TOKEN(HttpStatus.NOT_ACCEPTABLE, "MEMBER_008", "Refresh token mismatch."),

    NOT_FOUND_ACCESS_TOKEN(HttpStatus.NOT_FOUND, "ACCESS_TOKEN_001", "Access token not found."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "ACCESS_TOKEN_002", "Access token is invalid."),
    EXPIRATION_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_003", "Access token has expired."),
    NOT_SUPPORTED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_004", "Access token is not supported."),
    UNKNOWN_ACCESS_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_005", "Access token error."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
