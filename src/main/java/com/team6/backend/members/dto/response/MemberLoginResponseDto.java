package com.team6.backend.members.dto.response;

import lombok.Getter;

@Getter
public class MemberLoginResponseDto {
    private Boolean isExistUserId;

    public MemberLoginResponseDto(Boolean isExistUserId) {
        this.isExistUserId = isExistUserId;
    }
}
