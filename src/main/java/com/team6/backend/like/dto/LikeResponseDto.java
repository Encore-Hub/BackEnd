package com.team6.backend.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponseDto {
    private String mt20id;
    private String email;
    private boolean liked;
    private long likeCount;
}
