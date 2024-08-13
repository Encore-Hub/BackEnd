package com.team6.backend.comment.dto;

import lombok.*;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private String email;
    private String mt20id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long parentCommentId;

}
