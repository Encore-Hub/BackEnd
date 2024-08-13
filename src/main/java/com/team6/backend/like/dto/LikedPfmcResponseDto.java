package com.team6.backend.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikedPfmcResponseDto { // 좋아요 상태
    private String mt20id;     // 공연 아이디
    private String prfnm;      // 공연명
    private String poster;     // 공연 포스터
    private boolean isLiked;   // 좋아요 상태
}
