package com.team6.backend.Oauth2.kakao.service;

import com.team6.backend.Oauth2.kakao.entity.KakaoMember;
import com.team6.backend.Oauth2.kakao.dto.KakaoDto;
import com.team6.backend.Oauth2.kakao.repository.KakaoMemberRepository;
import com.team6.backend.security.jwt.JwtUtil; // 추가된 JwtUtil import
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
@Service
public class KakaoService {

    private final KakaoMemberRepository kakaoMemberRepository;
    private final JwtUtil jwtUtil; // 추가된 JwtUtil 필드

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${kakao.redirect-url}")
    private String kakaoRedirectUrl;

    private final static String KAKAO_AUTH_URL = "https://kauth.kakao.com";
    private final static String KAKAO_API_URL = "https://kapi.kakao.com";

    public String getKakaoLogin() {
        return KAKAO_AUTH_URL + "/oauth/authorize?client_id=" + kakaoClientId + "&redirect_uri=" + kakaoRedirectUrl + "&response_type=code";
    }

    public String getKakaoToken(String code) throws Exception {
        if (code == null) {
            throw new IllegalArgumentException("인증코드 오류");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", kakaoClientId);
            params.add("client_secret", kakaoClientSecret);
            params.add("code", code);
            params.add("redirect_uri", kakaoRedirectUrl);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(KAKAO_AUTH_URL + "/oauth/token", HttpMethod.POST, request, String.class);

            JSONObject jsonObj = new JSONObject(response.getBody());
            return jsonObj.getString("access_token");
        } catch (Exception e) {
            throw new Exception("API를 불러오지 못함", e);
        }
    }

    public KakaoDto getKakaoInfo(String code) throws Exception {
        String accessToken = getKakaoToken(code);
        return getUserInfoWithToken(accessToken);
    }

    private KakaoDto getUserInfoWithToken(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(KAKAO_API_URL + "/v2/user/me", HttpMethod.GET, request, String.class);

        JSONObject jsonObj = new JSONObject(response.getBody());
        JSONObject account = jsonObj.getJSONObject("kakao_account");
        JSONObject profile = account.getJSONObject("profile");

        long id = jsonObj.getLong("id");
        String email = account.getString("email");
        String nickname = profile.getString("nickname");

        KakaoMember kakaomember = new KakaoMember();
        kakaomember.setEmail(email);
        kakaomember.setUsername(nickname);
        kakaoMemberRepository.save(kakaomember);

        // JWT 토큰 생성
        String jwtToken = jwtUtil.createAccessToken(email, kakaomember.getRole()); // 여기서 역할 설정 필요

        // 세션에 KakaoMember 저장 (원하는 경우)
        HttpServletRequest kakaorequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = kakaorequest.getSession();
        session.setAttribute("KakaoMember", kakaomember);

        // KakaoDto 반환과 함께 JWT 토큰 포함
        return KakaoDto.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .jwtToken(jwtToken) // JWT 토큰을 KakaoDto에 추가
                .build();
    }
}
