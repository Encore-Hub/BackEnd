package com.team6.backend.security.jwt;

import com.team6.backend.common.exception.ErrorCode;
import com.team6.backend.member.entity.MemberRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_ACCESS = "AccessToken";
    public static final String AUTHORIZATION_REFRESH = "RefreshToken";
    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESSTOKEN_TIME = 30 * 60 * 1000L; // Access Token 만료 시간
    private static final long REFRESHTOKEN_TIME = 60 * 24 * 60 * 60 * 1000L; // Refresh Token 토큰 만료 시간

    @Value("${spring.jwt.secret.key.access}")
    private String accessTokenSecretKey;
    @Value("${spring.jwt.secret.key.refresh}")
    private String refreshTokenSecretKey;
    private Key accessTokenKey;
    private Key refreshTokenKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] accessTokenBytes = Base64.getDecoder().decode(accessTokenSecretKey);
        accessTokenKey = Keys.hmacShaKeyFor(accessTokenBytes);

        byte[] refreshTokenBytes = Base64.getDecoder().decode(refreshTokenSecretKey);
        refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenBytes);
    }

    // JWT(Access Token) 생성
    public String createAccessToken(String email, MemberRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + ACCESSTOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(accessTokenKey, signatureAlgorithm)
                        .compact();
    }

    // JWT(Refresh Token) 생성
    public String createRefreshToken() {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setExpiration(new Date(date.getTime() + REFRESHTOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(refreshTokenKey, signatureAlgorithm)
                        .compact();
    }

    // JWT 토큰 substring
    public String substringToken(HttpServletRequest request, String authorization) {
        String bearerToken = request.getHeader(authorization);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }


    public boolean validateAccessToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            //헤더에는 JWT를 추출하고, 서명을 검증
            Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(request.getHeader(AUTHORIZATION_ACCESS).substring(7));
            return true;
            //서명검증 실패시
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.INVALID_ACCESS_TOKEN.getCode());
            //jwt 만료
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.EXPIRATION_ACCESS_TOKEN.getCode());
            // 지원되지않는 형식
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.NOT_SUPPORTED_ACCESS_TOKEN.getCode());
            //일반적인 오류처리
        } catch (JwtException e) {
            e.printStackTrace();
            request.setAttribute("exception", ErrorCode.UNKNOWN_ACCESS_TOKEN_ERROR.getCode());
        }

        return false;
    }

    // JWT(토큰)에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(token).getBody();
    }
}
