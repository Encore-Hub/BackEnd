package com.team6.backend.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {

    private Long id;
    private String content;
    private String email;
    private String mt20id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long parentCommentId; // 부모 댓글 ID

    public CommentResponseDto(Long id, String content, String email, String mt20id,
                              LocalDateTime createdAt, LocalDateTime updatedAt, Long parentCommentId) {
        this.id = id;
        this.content = content;
        this.email = email; // 이메일 필드로 변경
        this.mt20id = mt20id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.parentCommentId = parentCommentId;
    }
}
