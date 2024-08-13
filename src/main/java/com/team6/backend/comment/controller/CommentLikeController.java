package com.team6.backend.comment.controller;

import com.team6.backend.comment.dto.CommentLikeRequestDto;
import com.team6.backend.comment.dto.CommentLikeResponseDto;
import com.team6.backend.comment.service.CommentLikeService;
import com.team6.backend.common.response.ResponseMessage;
import com.team6.backend.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/comment/likes")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;
    private final JwtUtil jwtUtil; // JWT 유틸 추가

    // 좋아요 등록 및 취소
    @PostMapping("/toggle")
    public ResponseEntity<ResponseMessage<Void>> toggleLike(@RequestBody CommentLikeRequestDto requestDto, HttpServletRequest request) {
        String email = jwtUtil.getEmailFromToken(jwtUtil.getAccessTokenFromHeader(request)); // 토큰에서 이메일 추출
        commentLikeService.toggleLike(requestDto, email);
        return new ResponseEntity<>(new ResponseMessage<>("좋아요 상태가 변경되었습니다.", null), HttpStatus.OK);
    }

    // 댓글의 좋아요 수 조회
    @GetMapping("/count")
    public ResponseEntity<ResponseMessage<CommentLikeResponseDto>> getLikeCount(@RequestParam Long commentId) {
        CommentLikeResponseDto responseDto = commentLikeService.getLikeCount(commentId);
        return new ResponseEntity<>(new ResponseMessage<>("좋아요 수 조회 성공", responseDto), HttpStatus.OK);
    }
}
