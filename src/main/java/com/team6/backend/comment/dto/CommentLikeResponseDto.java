package com.team6.backend.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikeResponseDto {
    private Long commentId;
    private Long likeCount;
    private boolean liked;

    // 두 개의 매개변수를 받는 생성자
    public CommentLikeResponseDto(Long commentId, Long likeCount) {
        this.commentId = commentId;
        this.likeCount = likeCount;
    }
}
