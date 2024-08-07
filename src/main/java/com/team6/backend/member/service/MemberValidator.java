package com.team6.backend.member.service;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.member.dto.request.MemberSignupRequestDto;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.team6.backend.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberValidator {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void validateSignup(MemberSignupRequestDto requestDto) {
        validateEmail(requestDto.getEmail()); //이메일 중복확인
        validateNickname(requestDto.getNickname()); // 닉네임 중복확인
        validatePhoneNumber(requestDto.getPhoneNumber()); // 핸드폰번호 중복확인
        confirmPassword(requestDto.getPassword(), requestDto.getConfirmPassword()); // 비밀번호와 비밀번호 확인값이 같은지?
    }


    public void validateEmail(String email) {
        Optional<Member> memberOpt = memberRepository.findByEmail(email);
        if (memberOpt.isPresent()) {
            throw new EncoreHubException(DUPLICATED_EMAIL);
        }
    }

    public void validatePhoneNumber(String phoneNumber) {
        Optional<Member> memberOpt = memberRepository.findByPhoneNumber(phoneNumber);
        if (memberOpt.isPresent()) {
            throw new EncoreHubException(DUPLICATED_PHONENUMBER);
        }
    }

    public void validateNickname(String nickname) {
        Optional<Member> memberOpt = memberRepository.findByNickname(nickname);
        if (memberOpt.isPresent()) {
            throw new EncoreHubException(DUPLICATED_NICKNAME);
        }
    }

    public void confirmPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new EncoreHubException(DUPLICATED_PASSWORD);
        }
    }

    public void validateMatchPassword(String storedPassword, String password){
        if(!passwordEncoder.matches(storedPassword, password)){
            throw new EncoreHubException(NOT_VALID_PASSWORD);
        }
    }

    public boolean validateExistEmail(Member member) {
        boolean isExistEmail = true;
        if (member.getEmail() == null) {
            isExistEmail = false;
        }
        return isExistEmail;
    }
}