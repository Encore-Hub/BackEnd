package com.team6.backend.favorite.theater.repository;

import com.team6.backend.favorite.theater.entity.FavoriteTheater;
import com.team6.backend.member.entity.Member;
import com.team6.backend.theater.theater.entity.TheaterDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteTheaterRepository extends JpaRepository<FavoriteTheater, Long> {
    // 수정된 메서드
    List<FavoriteTheater> findByMemberAndTheaterDetail(Member member, TheaterDetail theaterDetail);

    List<FavoriteTheater> findByTheaterDetail(TheaterDetail theaterDetail);

    List<FavoriteTheater> findByMember(Member member);
}
