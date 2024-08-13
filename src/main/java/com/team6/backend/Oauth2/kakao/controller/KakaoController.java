package com.team6.backend.Oauth2.kakao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team6.backend.Oauth2.kakao.dto.KakaoDto;
import com.team6.backend.Oauth2.kakao.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;



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
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) throws Exception {
        KakaoDto kakaoDto = kakaoService.getKakaoInfo(code);

        // 헤더에 JWT 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoDto.getJwtToken());

        // KakaoDto 객체를 JSON 문자열로 변환
        String kakaoDtoJson = convertToJson(kakaoDto);

        // 본문에 JSON 데이터를 담아 반환
        return new ResponseEntity<>(kakaoDtoJson, headers, HttpStatus.OK);
    }

    // KakaoDto를 JSON 문자열로 변환하는 메서드
    private String convertToJson(KakaoDto kakaoDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(kakaoDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}"; // 변환 실패 시 빈 JSON 반환
        }
    }
}
