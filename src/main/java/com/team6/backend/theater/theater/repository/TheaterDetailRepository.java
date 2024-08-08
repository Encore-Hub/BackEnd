package com.team6.backend.theater.theater.repository;

import com.team6.backend.theater.theater.entity.TheaterDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TheaterDetailRepository extends JpaRepository<TheaterDetail, String> {
    List<TheaterDetail> findByFcltynmContaining(String name); // 공연장 이름으로 검색
    Optional<TheaterDetail> findByMt10id(String mt10id);
}
