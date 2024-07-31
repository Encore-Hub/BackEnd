package com.team6.backend.member.dto.request;

import com.team6.backend.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberSignupRequestDto {
    private String email;
    private String password;
    private String confirmPassword;
    private String username;
    private String nickname;
    private String phoneNumber;

    public Member toEntity(String password) {
        return Member.builder()
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .role("USER")
                .build();
    }
}
