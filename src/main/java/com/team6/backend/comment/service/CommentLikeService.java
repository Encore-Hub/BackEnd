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
    public CommentLikeResponseDto toggleLike(CommentLikeRequestDto requestDto, String email) {
        Comment comment = commentRepository.findById(requestDto.getCommentId())
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        CommentLike existingLike = commentLikeRepository.findByCommentIdAndMemberEmail(requestDto.getCommentId(), email)
                .orElse(null);

        boolean liked;
        if (existingLike == null) {
            // 좋아요가 없는 경우 추가
            CommentLike like = new CommentLike(comment, member);
            commentLikeRepository.save(like);
            liked = true;
        } else {
            // 이미 좋아요가 있는 경우 상태를 토글
            existingLike.toggleLike();
            if (existingLike.isLiked()) {
                commentLikeRepository.save(existingLike);
                liked = true;
            } else {
                commentLikeRepository.delete(existingLike);
                liked = false;
            }
        }

        // 최종 좋아요 수를 반환
        long likeCount = commentLikeRepository.countByCommentId(requestDto.getCommentId());
        return new CommentLikeResponseDto(requestDto.getCommentId(), likeCount, liked);
    }

    public CommentLikeResponseDto getLikeCount(Long commentId) {
        long likeCount = commentLikeRepository.countByCommentId(commentId);
        return new CommentLikeResponseDto(commentId, likeCount);
    }
}
