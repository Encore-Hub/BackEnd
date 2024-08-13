package com.team6.backend.Oauth2.kakao.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class KakaoDto {

    private long id;
    private String email;
    private String nickname;
    private String jwtToken;

}
