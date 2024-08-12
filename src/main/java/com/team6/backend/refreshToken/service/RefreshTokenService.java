package com.team6.backend.refreshToken.service;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import com.team6.backend.refreshToken.entity.RefreshToken;
import com.team6.backend.refreshToken.repository.RefreshTokenRepository;
import com.team6.backend.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.team6.backend.common.exception.ErrorCode.*;


@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    /**
     * refresh token 이 유효하고 동일한지 확인한다.
     * 만약 올바른 토큰이라면 access token 을 발급하고,
     * 그렇지 않다면 refresh 토큰 삭제 후 다시 로그인하도록 응답한다.
     * @param refreshToken 확인할 refresh token
     * @return 유효한 refresh token 이라면 새로운 access token 반환
     */
    @Transactional
    public String getAccess(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new EncoreHubException(EXPIRATION_REFRESH_TOKEN));
        if (!token.getRefreshToken().equals(refreshToken)) {
            refreshTokenRepository.delete(token);
            throw new EncoreHubException(INVALID_REFRESH_TOKEN);
        }

        Member member = memberRepository.findById(token.getMemberId())
                .orElseThrow(() -> new EncoreHubException(NOT_FOUND_MEMBER));
        return jwtUtil.createAccessToken(member.getEmail(), member.getRole());
    }
}