package com.team6.backend.comment.dto;

import lombok.Data;

@Data
public class CommentLikeRequestDto {
    private Long commentId;
    private Long memberId;
}
