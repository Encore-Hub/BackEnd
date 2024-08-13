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
}
