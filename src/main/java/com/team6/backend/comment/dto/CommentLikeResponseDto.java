package com.team6.backend.comment.dto;

import lombok.Data;

@Data
public class CommentLikeResponseDto {
    private Long commentId;
    private Long likeCount;

    public CommentLikeResponseDto(Long commentId, Long likeCount) {
        this.commentId = commentId;
        this.likeCount = likeCount;
    }
}
