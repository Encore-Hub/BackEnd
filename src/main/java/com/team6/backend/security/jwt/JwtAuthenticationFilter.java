package com.team6.backend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.common.exception.ErrorCode;
import com.team6.backend.common.response.ResponseMessage;
import com.team6.backend.member.dto.request.MemberLoginRequestDto;
import com.team6.backend.member.entity.MemberRoleEnum;
import com.team6.backend.refreshToken.entity.RefreshToken;
import com.team6.backend.refreshToken.repository.RefreshTokenRepository;
import com.team6.backend.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

import static com.team6.backend.common.exception.ErrorCode.FAILED_LOGIN;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // 인증
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/api/member/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            MemberLoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), MemberLoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");

        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        MemberRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getMember().getRole();
        Long id = ((UserDetailsImpl) authResult.getPrincipal()).getMember().getId();

        String accessToken = jwtUtil.createAccessToken(username, role);
        String refreshToken = jwtUtil.createRefreshToken(id);

        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(JwtUtil.AUTHORIZATION_ACCESS, accessToken);
        response.addHeader(JwtUtil.AUTHORIZATION_REFRESH, refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(new ResponseMessage<>("로그인 성공", null));
        PrintWriter out = response.getWriter();
        out.print(jsonString);
        out.flush();


        refreshTokenRepository.save(new RefreshToken(refreshToken, id));



    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(new EncoreHubException(FAILED_LOGIN));
        PrintWriter out = response.getWriter();
        out.print(jsonString);
        out.flush();

    }
}
