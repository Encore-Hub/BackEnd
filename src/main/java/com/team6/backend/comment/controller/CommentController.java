package com.team6.backend.comment.controller;

import com.team6.backend.comment.dto.CommentRequestDto;
import com.team6.backend.comment.dto.CommentResponseDto;
import com.team6.backend.comment.entity.Comment;
import com.team6.backend.comment.service.CommentService;
import com.team6.backend.common.response.ResponseMessage;
import com.team6.backend.member.entity.MemberRoleEnum;
import com.team6.backend.pfmc.entity.Pfmc;
import com.team6.backend.security.jwt.JwtUtil; // 추가된 임포트
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtUtil jwtUtil; // 추가된 필드

    // 댓글 추가
    @PostMapping
    public ResponseEntity<ResponseMessage<CommentResponseDto>> addComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        String email = jwtUtil.getEmailFromToken(jwtUtil.getAccessTokenFromHeader(request)); // 토큰에서 이메일 추출
        CommentResponseDto responseDto = commentService.addComment(requestDto, email);
        return ResponseEntity.ok(new ResponseMessage<>("댓글이 추가되었습니다.", responseDto));
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseMessage<CommentResponseDto>> updateComment(@PathVariable Long commentId,
                                                                             HttpServletRequest request,
                                                                             @RequestBody CommentRequestDto requestDto) {
        String email = jwtUtil.getEmailFromToken(jwtUtil.getAccessTokenFromHeader(request)); // 토큰에서 이메일 추출
        CommentResponseDto responseDto = commentService.updateComment(commentId, email, requestDto.getContent());
        return ResponseEntity.ok(new ResponseMessage<>("댓글이 수정되었습니다.", responseDto));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseMessage<Void>> deleteComment(@PathVariable Long commentId,
                                                               HttpServletRequest request) {
        String email = jwtUtil.getEmailFromToken(jwtUtil.getAccessTokenFromHeader(request)); // 토큰에서 이메일 추출
        commentService.deleteComment(commentId, email);
        return ResponseEntity.ok(new ResponseMessage<>("댓글이 삭제되었습니다.", null));
    }

    // 공연에 대한 모든 댓글 조회
    @GetMapping("/performance/{mt20id}")
    public ResponseEntity<ResponseMessage<List<CommentResponseDto>>> getCommentsByPerformance(@PathVariable String mt20id) {
        List<CommentResponseDto> responseDtos = commentService.getCommentsByPerformance(mt20id);
        return ResponseEntity.ok(new ResponseMessage<>("공연에 대한 댓글 조회 성공", responseDtos));
    }

    // 특정 회원이 작성한 댓글을 기반으로 공연 목록 조회
    @GetMapping("/mypage/performances")
    public ResponseEntity<ResponseMessage<List<Pfmc>>> getPerformancesByMemberComments(HttpServletRequest request) {
        String email = jwtUtil.getEmailFromToken(jwtUtil.getAccessTokenFromHeader(request)); // 토큰에서 이메일 추출
        List<Pfmc> performances = commentService.getPerformancesByMemberComments(email);
        return ResponseEntity.ok(new ResponseMessage<>("회원이 작성한 댓글 기반 공연 목록 조회 성공", performances));
    }

    // 공연에 대한 상위 부모 댓글 조회
    @GetMapping("/performance/{mt20id}/toplike-comments")
    public ResponseEntity<ResponseMessage<List<Comment>>> getTopParentCommentsByPerformance(@PathVariable String mt20id) {
        List<Comment> comments = commentService.getParentCommentsByPerformanceOrderedByLikes(mt20id);
        return ResponseEntity.ok(new ResponseMessage<>("공연에 대한 상위 댓글 조회 성공", comments));
    }

    // 관리자용 댓글 수정
    @Secured(MemberRoleEnum.Authority.ADMIN)
    @PutMapping("/admin/{commentId}")
    public ResponseEntity<ResponseMessage<CommentResponseDto>> updateCommentAsAdmin(@PathVariable Long commentId,
                                                                                    @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto responseDto = commentService.updateCommentAsAdmin(commentId, requestDto.getContent());
        return ResponseEntity.ok(new ResponseMessage<>("댓글이 관리자에 의해 수정되었습니다.", responseDto));
    }

    // 관리자용 댓글 삭제
    @Secured(MemberRoleEnum.Authority.ADMIN)
    @DeleteMapping("/admin/{commentId}")
    public ResponseEntity<ResponseMessage<Void>> deleteCommentAsAdmin(@PathVariable Long commentId) {
        commentService.deleteCommentAsAdmin(commentId);
        return ResponseEntity.ok(new ResponseMessage<>("댓글이 관리자에 의해 삭제되었습니다.", null));
    }
}
