package com.team6.backend.security.jwt;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.common.exception.ErrorCode;
import com.team6.backend.refreshToken.entity.RefreshToken;
import com.team6.backend.refreshToken.repository.RefreshTokenRepository;
import com.team6.backend.security.UserDetailsImpl;
import com.team6.backend.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    // 인가

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.getAccessTokenFromHeader(req);
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(req);

        // Access Token이 있다면
        if (StringUtils.hasText(accessToken)) {

            log.info("Access Token: {}", accessToken);
            if (jwtUtil.validateAccessToken(accessToken, req)) {
                log.info("Access Token is Validated");
                Claims info = jwtUtil.getUserInfoFormToken(accessToken);
                try {
                    setAuthentication(info.getSubject());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }

            }
            else if (StringUtils.hasText(refreshToken)) {

                log.info("Refresh Token: {}", refreshToken);
                if (jwtUtil.validateAccessToken(refreshToken, req)) {
                    // Redis에서 Refresh Token 확인
                    RefreshToken storedRefreshToken = refreshTokenRepository.findById(refreshToken)
                            .orElseThrow(() -> new EncoreHubException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));
                    Long storedMemberId = storedRefreshToken.getMemberId();
                    Claims claims = jwtUtil.getUserInfoFormToken(refreshToken);
                    Long tokenMemberId = claims.get("id", Long.class);

                    if (tokenMemberId.equals(storedMemberId)) {
                        // 새로운 Access Token 생성
                        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserById(tokenMemberId);
                        String newAccessToken = jwtUtil.createAccessToken(userDetails.getUsername(), userDetails.getMember().getRole());
                        // 응답 헤더에 새로운 Access Token 추가
                        res.addHeader(JwtUtil.AUTHORIZATION_ACCESS, newAccessToken);
                        // 인증 처리
                        setAuthentication(userDetails.getUsername());
                    }
                    else {
                        log.error("Invalid Refresh Token");
                    }
                }
                else {
                    log.error("Refresh Token Error");
                }

            }

        }
        filterChain.doFilter(req, res);

    }

    // 인증 처리
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}