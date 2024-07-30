package com.team6.backend.members.service;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.config.jwt.JwtUtil;
import com.team6.backend.members.dto.request.MemberLoginRequestDto;
import com.team6.backend.members.dto.request.MemberSignupRequestDto;
import com.team6.backend.members.dto.response.MemberLoginResponseDto;
import com.team6.backend.members.entity.Member;
import com.team6.backend.members.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.team6.backend.common.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.team6.backend.config.jwt.JwtUtil.AUTHORIZATION_HEADER;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberValidator memberValidator;
    private final JwtUtil jwtUtil;

    public void signup(MemberSignupRequestDto requestDto) {
        memberValidator.validateEmail(requestDto.getEmail());
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = requestDto.toEntity(encodedPassword);
        memberRepository.save(member);
    }

    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto, HttpServletResponse response) {
        Member member = memberRepository.findByUserId(requestDto.getUserId()).orElseThrow(
                () -> new EncoreHubException(NOT_FOUND_MEMBER)
        );
        memberValidator.validateMatchPassword(requestDto.getPassword(), member.getPassword());
        response.addHeader(AUTHORIZATION_HEADER, jwtUtil.createToken(member.getUserId(), member.getRole()));
        boolean isExistUserId = memberValidator.validateExistUserId(member);
        return new MemberLoginResponseDto(isExistUserId);
    }
}
