package com.team6.backend.Oauth2.kakao.controller;

import com.team6.backend.Oauth2.kakao.dto.KakaoDto;
import com.team6.backend.Oauth2.kakao.entity.MsgEntity;
import com.team6.backend.Oauth2.kakao.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/login/kakao")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoService.getKakaoLogin());
    }

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<KakaoDto> kakaoCallback(@RequestParam String code) throws Exception {
        KakaoDto kakaoDto = kakaoService.getKakaoInfo(code);
        return ResponseEntity.ok(kakaoDto);
    }
}
