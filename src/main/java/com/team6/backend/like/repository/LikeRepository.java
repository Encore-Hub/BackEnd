package com.team6.backend.like.repository;

import com.team6.backend.like.entity.Like;
import com.team6.backend.member.entity.Member;
import com.team6.backend.pfmc.entity.Pfmc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    //특정 사용자와 공연에 대한 Like 엔티티를 조회하는 메소드
    Optional<Like> findByMemberAndPfmc(Member member, Pfmc pfmc);

    // 특정 사용자가 좋아요한 모든 Like 엔티티를 조회하는 메소드
    List<Like> findByMember(Member member);

    // 특정 공연에 좋아요가 눌린 총 개수를 조회하는 메소드
    long countByPfmcAndLiked(Pfmc pfmc, boolean liked);

}
