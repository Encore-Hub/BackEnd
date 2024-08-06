package com.team6.backend.comment.service;

import com.team6.backend.comment.dto.CommentLikeRequestDto;
import com.team6.backend.comment.dto.CommentLikeResponseDto;
import com.team6.backend.comment.entity.Comment;
import com.team6.backend.comment.entity.CommentLike;
import com.team6.backend.comment.repository.CommentLikeRepository;
import com.team6.backend.comment.repository.CommentRepository;
import com.team6.backend.common.exception.ResourceNotFoundException;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommentLikeResponseDto toggleLike(CommentLikeRequestDto requestDto) {
        Comment comment = commentRepository.findById(requestDto.getCommentId())
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        CommentLike existingLike = commentLikeRepository.findByCommentIdAndMemberId(requestDto.getCommentId(), requestDto.getMemberId()).orElse(null);

        if (existingLike == null) {
            // 좋아요가 없는 경우 추가
            CommentLike like = new CommentLike(comment, member);
            commentLikeRepository.save(like);
        } else {
            // 이미 좋아요가 있는 경우 제거
            commentLikeRepository.delete(existingLike);
        }

        // 최종 좋아요 수를 반환
        long likeCount = commentLikeRepository.countByCommentId(requestDto.getCommentId());
        return new CommentLikeResponseDto(requestDto.getCommentId(), likeCount);
    }

    public CommentLikeResponseDto getLikeCount(Long commentId) {
        long likeCount = commentLikeRepository.countByCommentId(commentId);
        return new CommentLikeResponseDto(commentId, likeCount);
    }

}
