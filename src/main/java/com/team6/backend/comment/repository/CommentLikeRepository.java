package com.team6.backend.comment.repository;

import com.team6.backend.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, Long memberId);
    long countByCommentId(Long commentId);

}
