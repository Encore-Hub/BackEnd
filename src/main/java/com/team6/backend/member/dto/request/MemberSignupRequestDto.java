package com.team6.backend.member.dto.request;

import com.team6.backend.member.entity.Member;
import com.team6.backend.member.entity.MemberRoleEnum;
import lombok.Getter;

@Getter
public class MemberSignupRequestDto {
    // 조건 걸어야한다.
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
                .role(MemberRoleEnum.USER)
                .build();
    }
}
