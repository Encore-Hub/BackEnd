package com.team6.backend.members.dto.request;

import lombok.Getter;

@Getter
public class MemberLoginRequestDto {
    private String userId;
    private String password;
}
