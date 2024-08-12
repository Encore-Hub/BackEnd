package com.team6.backend.Oauth2.kakao.controller;

import com.team6.backend.Oauth2.kakao.dto.KakaoDto;
import com.team6.backend.Oauth2.kakao.Entity.MsgEntity;
import com.team6.backend.Oauth2.kakao.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/callback")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) throws Exception {
        KakaoDto kakaoInfo = kakaoService.getKakaoInfo(request.getParameter("code"));
        return ResponseEntity.ok()
                .body(new MsgEntity("success", kakaoInfo));

    }

}
