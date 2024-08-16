package com.team6.backend.member.dto.request;

import com.team6.backend.member.entity.Member;
import com.team6.backend.member.entity.MemberRoleEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class MemberSignupRequestDto {
    @Email(message = "유효한 이메일 주소여야 합니다.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "비밀번호는 하나 이상의 대문자, 소문자, 숫자, 특수 문자를 포함해야 합니다.")
    private String password;
    private String confirmPassword;
    private String username;
    private String nickname;
    @NotBlank(message = "핸드폰 번호는 필수 항목입니다.")
    @Pattern(regexp = "^(010|011|016|017|018|019)[-]?[0-9]{3,4}[-]?[0-9]{4}$",
            message = "유효한 핸드폰 번호를 입력해 주세요. (예: 010-1234-5678 )")
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
