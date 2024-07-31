package com.team6.backend.member.dto.response;

import lombok.Getter;

@Getter
public class MemberLoginResponseDto {
    private Boolean isExistEmail;

    public MemberLoginResponseDto(Boolean isExistUserId) {
        this.isExistEmail = isExistUserId;
    }
}
