package com.team6.backend.members.service;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.members.entity.Member;
import com.team6.backend.members.repository.MemberRepository;
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

    public void validateUserId(String userId) {
        Optional<Member> memberOpt = memberRepository.findByUserId(userId);
        if (memberOpt.isPresent()) {
            throw new EncoreHubException(DUPLICATED_USERID);
        }
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

    public void validateMatchPassword(String storedPassword, String password){
        if(!passwordEncoder.matches(storedPassword, password)){
            throw new EncoreHubException(NOT_VALID_PASSWORD);
        }
    }

    public boolean validateExistUserId(Member member) {
        boolean isExistUserId = true;
        if (member.getUserId() == null) {
            isExistUserId = false;
        }
        return isExistUserId;
    }


}