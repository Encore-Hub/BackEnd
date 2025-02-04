package com.team6.backend.member.service;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.config.jwt.JwtUtil;
import com.team6.backend.member.dto.request.MemberLoginRequestDto;
import com.team6.backend.member.dto.request.MemberSignupRequestDto;
import com.team6.backend.member.dto.response.MemberLoginResponseDto;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import com.team6.backend.redis.service.RedisService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.team6.backend.common.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.team6.backend.config.jwt.JwtUtil.AUTHORIZATION_ACCESS;
import static com.team6.backend.config.jwt.JwtUtil.AUTHORIZATION_REFRESH;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberValidator memberValidator;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    // 회원가입
    public void signup(MemberSignupRequestDto requestDto) {
        memberValidator.validateSignup(requestDto);
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = requestDto.toEntity(encodedPassword);
        memberRepository.save(member);
    }

    // 로그인
    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new EncoreHubException(NOT_FOUND_MEMBER)
        );
        memberValidator.validateMatchPassword(requestDto.getPassword(), member.getPassword());

        String accessToken = jwtUtil.createAccessToken(member.getEmail(), member.getRole());
        String refreshToken = jwtUtil.createRefreshToken();
        response.addHeader(AUTHORIZATION_ACCESS, accessToken);
        response.addHeader(AUTHORIZATION_REFRESH, refreshToken);
        redisService.setValues(requestDto.getEmail(), refreshToken, Duration.ofDays(15));

        boolean isExistEmail = memberValidator.validateExistEmail(member);
        return new MemberLoginResponseDto(isExistEmail);
    }
}
