package com.team6.backend.members.dto.request;

import com.team6.backend.members.entity.Member;
import lombok.Getter;

@Getter
public class MemberSignupRequestDto {
    private String name;
    private String userId;
    private String password;
    private String email;
    private String nickname;
    private String phoneNumber;

    public Member toEntity(String password) {
        return Member.builder()
                .name(name)
                .userId(userId)
                .password(password)
                .email(email)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .role("USER")
                .build();
    }
}
