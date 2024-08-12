package com.team6.backend.like.dto;

import com.team6.backend.pfmc.entity.Pfmc;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikedPfmcResponseDto {
    private Pfmc pfmc;
    private boolean liked; // 좋아요 상태
}

