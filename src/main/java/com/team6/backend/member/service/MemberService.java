package com.team6.backend.member.service;

import com.team6.backend.member.dto.request.MemberSignupRequestDto;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberValidator memberValidator;


    // 회원가입
    public void signup(MemberSignupRequestDto requestDto) {
        memberValidator.validateSignup(requestDto);
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = requestDto.toEntity(encodedPassword);
        memberRepository.save(member);
    }


}


