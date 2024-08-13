package com.team6.backend.comment.dto;

import lombok.Data;

@Data
public class CommentRequestDto {
    private String mt20id;
    private String content;
    private Long parentCommentId; // 대댓글 작성 시 부모 댓글 ID
}
