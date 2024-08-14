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
    public String KakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = kakaoService.KakaoLogin(code);

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_ACCESS, token.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        // 사용자를 리다이렉트
        return "redirect:/";
    }
}