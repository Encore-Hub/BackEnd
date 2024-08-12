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
    public CommentResponseDto addComment(CommentRequestDto requestDto, String email) {
        Pfmc performance = pfmcRepository.findById(requestDto.getMt20id())
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment parentComment = null;
        if (requestDto.getParentCommentId() != null) {
            parentComment = commentRepository.findById(requestDto.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
        }

        Comment comment = new Comment(requestDto.getContent(), member, performance, parentComment);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment.getId(), savedComment.getContent(),
                savedComment.getMember().getEmail(), savedComment.getPfmc().getMt20id(),
                savedComment.getCreatedAt(), savedComment.getUpdatedAt(),
                savedComment.getParentComment() != null ? savedComment.getParentComment().getId() : null);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, String memberEmail, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getMember().getEmail().equals(memberEmail)) {
            throw new UnauthorizedException("You are not allowed to edit this comment");
        }

        comment.updateContent(newContent);
        Comment updatedComment = commentRepository.save(comment);

        return new CommentResponseDto(updatedComment.getId(), updatedComment.getContent(),
                updatedComment.getMember().getEmail(), updatedComment.getPfmc().getMt20id(),
                updatedComment.getCreatedAt(), updatedComment.getUpdatedAt(),
                updatedComment.getParentComment() != null ? updatedComment.getParentComment().getId() : null);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, String memberEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getMember().getEmail().equals(memberEmail)) {
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
                        comment.getMember().getEmail(), comment.getPfmc().getMt20id(),
                        comment.getCreatedAt(), comment.getUpdatedAt(),
                        comment.getParentComment() != null ? comment.getParentComment().getId() : null))
                .collect(Collectors.toList());
    }

    // 특정 회원이 작성한 댓글을 기반으로 공연 목록 조회
    public List<Pfmc> getPerformancesByMemberComments(String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Comment> comments = commentRepository.findByMember(member);

        return comments.stream()
                .map(Comment::getPfmc)
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Comment> getParentCommentsByPerformanceOrderedByLikes(String mt20id) {
        Pfmc performance = pfmcRepository.findById(mt20id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        return commentRepository.findParentCommentsByPerformanceOrderedByLikes(performance);
    }

    // 관리자용 댓글 수정
    @Transactional
    public CommentResponseDto updateCommentAsAdmin(Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        comment.updateContent(newContent);
        Comment updatedComment = commentRepository.save(comment);

        return new CommentResponseDto(updatedComment.getId(), updatedComment.getContent(),
                updatedComment.getMember().getEmail(), updatedComment.getPfmc().getMt20id(),
                updatedComment.getCreatedAt(), updatedComment.getUpdatedAt(),
                updatedComment.getParentComment() != null ? updatedComment.getParentComment().getId() : null);
    }

    // 관리자용 댓글 삭제
    @Transactional
    public void deleteCommentAsAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        commentRepository.delete(comment);
    }
}
