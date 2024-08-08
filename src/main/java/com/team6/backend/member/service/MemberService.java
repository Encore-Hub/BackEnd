package com.team6.backend.member.service;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.security.jwt.JwtUtil;
import com.team6.backend.member.dto.request.MemberLoginRequestDto;
import com.team6.backend.member.dto.request.MemberSignupRequestDto;
import com.team6.backend.member.dto.response.MemberLoginResponseDto;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import com.team6.backend.redis.service.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.team6.backend.common.exception.ErrorCode.MISMATCH_REFRESH_TOKEN;
import static com.team6.backend.common.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.team6.backend.security.jwt.JwtUtil.AUTHORIZATION_ACCESS;
import static com.team6.backend.security.jwt.JwtUtil.AUTHORIZATION_REFRESH;

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
        boolean isExistEmail = memberValidator.validateExistEmail(member);
        // 로그인 성공 시 토큰 발급
        if (isExistEmail) {
            issueTokens(member, response);
        }
        return new MemberLoginResponseDto(isExistEmail);


    }
    // 토큰 발행
    public void issueTokens(Member member, HttpServletResponse response) {
        String accessToken = jwtUtil.createAccessToken(member.getEmail(), member.getRole());
        String refreshToken = jwtUtil.createRefreshToken();
        response.addHeader(AUTHORIZATION_ACCESS, accessToken);
        response.addHeader(AUTHORIZATION_REFRESH, refreshToken);
        redisService.setValues(member.getEmail(), refreshToken, Duration.ofDays(15));
    }

    // 토큰 재발행
    public void reIssueToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshTokenFromRequest = request.getHeader(AUTHORIZATION_REFRESH);
        String token = jwtUtil.substringToken(request, request.getHeader(AUTHORIZATION_ACCESS));

        Claims info = jwtUtil.getUserInfoFromToken(token);
        String email = info.getSubject();
        String refreshTokenFromRedis = redisService.getValues(email);

        if (refreshTokenFromRedis.equals(refreshTokenFromRequest)) {
            Member member = memberRepository.findByEmail(email).orElseThrow(
                    () -> new EncoreHubException(NOT_FOUND_MEMBER)
            );
            issueTokens(member, response);
        } else {
            throw new EncoreHubException(MISMATCH_REFRESH_TOKEN);
        }
    }
}


