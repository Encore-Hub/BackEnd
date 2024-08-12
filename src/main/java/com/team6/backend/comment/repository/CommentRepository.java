package com.team6.backend.comment.repository;

import com.team6.backend.comment.entity.Comment;
import com.team6.backend.member.entity.Member;
import com.team6.backend.pfmc.entity.Pfmc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 이메일로 회원을 찾고 그 회원이 작성한 댓글을 조회하는 메서드
    List<Comment> findByMember(Member member);
    // 특정 공연에 대한 모든 댓글을 조회하는 메서드
    List<Comment> findByPfmc(Pfmc pfmc);

    // 부모 댓글이 없는 댓글(최상위 댓글)만 조회하고, 좋아요 수에 따라 내림차순으로 정렬하는 쿼리
    @Query("SELECT c FROM Comment c WHERE c.pfmc = :performance AND c.parentComment IS NULL ORDER BY c.likes DESC")
    List<Comment> findParentCommentsByPerformanceOrderedByLikes(Pfmc performance);
}
