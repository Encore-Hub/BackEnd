package com.team6.backend.comment.service;

import com.team6.backend.comment.dto.CommentRequestDto;
import com.team6.backend.comment.dto.CommentResponseDto;
import com.team6.backend.comment.entity.Comment;
import com.team6.backend.comment.repository.CommentRepository;
import com.team6.backend.common.exception.ResourceNotFoundException;
import com.team6.backend.common.exception.UnauthorizedException;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import com.team6.backend.pfmc.entity.Pfmc;
import com.team6.backend.pfmc.repository.PfmcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PfmcRepository pfmcRepository;

    // 댓글 추가
    @Transactional
    public CommentResponseDto addComment(CommentRequestDto requestDto) {
        Pfmc performance = pfmcRepository.findById(requestDto.getMt20id())
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment parentComment = null;
        if (requestDto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(requestDto.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
        }

        Comment comment = new Comment(requestDto.getContent(), member, performance, parentComment);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment.getId(), savedComment.getContent(),
                savedComment.getMember().getId(), savedComment.getPfmc().getMt20id(),
                savedComment.getCreatedAt(), savedComment.getUpdatedAt(),
                savedComment.getParentComment() != null ? savedComment.getParentComment().getId() : null);
    }



    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, Long memberId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("You are not allowed to edit this comment");
        }

        comment.updateContent(newContent);
        Comment updatedComment = commentRepository.save(comment);

        return new CommentResponseDto(updatedComment.getId(), updatedComment.getContent(),
                updatedComment.getMember().getId(), updatedComment.getPfmc().getMt20id(),
                updatedComment.getCreatedAt(), updatedComment.getUpdatedAt(),
                updatedComment.getParentComment() != null ? updatedComment.getParentComment().getId() : null);
    }


    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }

    // 공연에 대한 모든 댓글 조회
    public List<CommentResponseDto> getCommentsByPerformance(String mt20id) {
        Pfmc pfmc = pfmcRepository.findById(mt20id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        List<Comment> comments = commentRepository.findByPfmc(pfmc);
        return comments.stream()
                .map(comment -> new CommentResponseDto(comment.getId(), comment.getContent(),
                        comment.getMember().getId(), comment.getPfmc().getMt20id(),
                        comment.getCreatedAt(), comment.getUpdatedAt(),
                        comment.getParentComment() != null ? comment.getParentComment().getId() : null))
                .collect(Collectors.toList());
    }


    // 특정 회원이 작성한 댓글을 기반으로 공연 목록 조회
    public List<Pfmc> getPerformancesByMemberComments(Long memberId) {
        // 회원 ID를 사용하여 회원 객체를 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 해당 회원이 작성한 모든 댓글을 조회
        List<Comment> comments = commentRepository.findByMember(member);

        // 각 댓글에 연결된 공연(Pfmc) 객체 추출
        return comments.stream()
                .map(Comment::getPfmc) // 각 댓글에서 공연 객체를 추출
                .distinct() // 중복된 공연 제거
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Comment> getParentCommentsByPerformanceOrderedByLikes(String Mt20id) {
        Pfmc performance = pfmcRepository.findById(Mt20id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        // 공연에 대한 부모 댓글을 좋아요 많은 순서로 조회
        return commentRepository.findParentCommentsByPerformanceOrderedByLikes(performance);
    }
}
