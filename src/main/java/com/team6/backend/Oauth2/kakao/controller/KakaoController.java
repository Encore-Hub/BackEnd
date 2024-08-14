package com.team6.backend.Oauth2.kakao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team6.backend.Oauth2.kakao.service.KakaoService;
import com.team6.backend.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.Cookie;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/oauth/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // KakaoService를 통해 로그인하고, 액세스 토큰과 리프레시 토큰을 포함하는 Map을 받아온다.
        Map<String, String> tokens = kakaoService.kakaoLogin(code);

        // Access Token과 Refresh Token을 Map에서 추출
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        // Access Token을 쿠키에 저장
        Cookie accessTokenCookie = new Cookie(JwtUtil.AUTHORIZATION_ACCESS, accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true); // 클라이언트 스크립트에서 접근 불가

        // Refresh Token을 쿠키에 저장
        Cookie refreshTokenCookie = new Cookie(JwtUtil.AUTHORIZATION_REFRESH, refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true); // 클라이언트 스크립트에서 접근 불가

        // 쿠키를 응답에 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 사용자를 리다이렉트
        return "redirect:/";
    }
}