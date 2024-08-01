package com.team6.backend.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    public static final String AUTHORIZATION_ACCESS = "AccessToken";
    public static final String AUTHORIZATION_REFRESH = "RefreshToken";
    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 30 * 60 * 1000L; // 토큰 만료 시간: 30분

    @Value("${spring.jwt.secret.key.access}")
    private String accessTokenSecretKey;
    @Value("${spring.jwt.secret.key.refresh}")
    private String refreshTokenSecretKey;
    private Key accessTokenKey;
    private Key refreshTokenKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    // secretKey를 Decoding해서 key에 담는다.
    @PostConstruct
    public void init() {
        byte[] accessTokenBytes = Base64.getDecoder().decode(accessTokenSecretKey);
        accessTokenKey = Keys.hmacShaKeyFor(accessTokenBytes);

        byte[] refreshTokenBytes = Base64.getDecoder().decode(refreshTokenSecretKey);
        refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenBytes);
    }

    // JWT(Access Token) 생성
    public String createAccessToken(String email, String role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(accessTokenKey, signatureAlgorithm) // 암호화 알고리즘 *****
                        .compact();
    }

    // JWT(Refresh Token) 생성
    public String createRefreshToken(){
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setExpiration(new Date(System.currentTimeMillis() + 60 * 24 * 60 * 60 * 1000L))
                        .setIssuedAt(date)
                        .signWith(refreshTokenKey, signatureAlgorithm)
                        .compact();
    }

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // JWT(토큰) 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(token); // 이 한줄로 검증
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // JWT(토큰)에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(token).getBody();
    }
}
