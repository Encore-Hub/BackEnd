package com.team6.backend.member.dto.request;

import lombok.Getter;

@Getter
public class MemberLoginRequestDto {
    private String email;
    private String password;
}
