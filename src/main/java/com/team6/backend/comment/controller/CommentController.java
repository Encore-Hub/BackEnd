package com.team6.backend.comment.controller;

import com.team6.backend.comment.dto.CommentRequestDto;
import com.team6.backend.comment.dto.CommentResponseDto;
import com.team6.backend.comment.entity.Comment;
import com.team6.backend.comment.service.CommentService;
import com.team6.backend.common.response.ResponseMessage;
import com.team6.backend.pfmc.entity.Pfmc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 추가
    @PostMapping
    public ResponseEntity<ResponseMessage<CommentResponseDto>> addComment(@RequestBody CommentRequestDto requestDto) {
        CommentResponseDto responseDto = commentService.addComment(requestDto);
        return ResponseEntity.ok(new ResponseMessage<>("댓글이 추가되었습니다.", responseDto));
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseMessage<CommentResponseDto>> updateComment(@PathVariable Long commentId,
                                                                             @RequestParam Long memberId,
                                                                             @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto responseDto = commentService.updateComment(commentId, memberId, requestDto.getContent());
        return ResponseEntity.ok(new ResponseMessage<>("댓글이 수정되었습니다.", responseDto));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseMessage<Void>> deleteComment(@PathVariable Long commentId,
                                                               @RequestParam Long memberId) {
        commentService.deleteComment(commentId, memberId);
        return ResponseEntity.ok(new ResponseMessage<>("댓글이 삭제되었습니다.", null));
    }

    // 공연에 대한 모든 댓글 조회
    @GetMapping("/performance/{mt20id}")
    public ResponseEntity<ResponseMessage<List<CommentResponseDto>>> getCommentsByPerformance(@PathVariable String mt20id) {
        List<CommentResponseDto> responseDtos = commentService.getCommentsByPerformance(mt20id);
        return ResponseEntity.ok(new ResponseMessage<>("공연에 대한 댓글 조회 성공", responseDtos));
    }

    // 특정 회원이 작성한 댓글을 기반으로 공연 목록 조회
    @GetMapping("/mypage/performances/{memberId}")
    public ResponseEntity<ResponseMessage<List<Pfmc>>> getPerformancesByMemberComments(@PathVariable Long memberId) {
        List<Pfmc> performances = commentService.getPerformancesByMemberComments(memberId);
        return ResponseEntity.ok(new ResponseMessage<>("회원이 작성한 댓글 기반 공연 목록 조회 성공", performances));
    }

    // 공연에 대한 상위 부모 댓글 조회
    @GetMapping("/performance/{mt20id}/top-comments")
    public ResponseEntity<ResponseMessage<List<Comment>>> getTopParentCommentsByPerformance(@PathVariable String mt20id) {
        List<Comment> comments = commentService.getParentCommentsByPerformanceOrderedByLikes(mt20id);
        return ResponseEntity.ok(new ResponseMessage<>("공연에 대한 상위 댓글 조회 성공", comments));
    }
}
