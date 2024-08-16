package com.team6.backend.theater.theater.repository;

import com.team6.backend.theater.theater.entity.TheaterPfmcDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheaterPfmcDetailRepository extends JpaRepository<TheaterPfmcDetail, String> {
    List<TheaterPfmcDetail> findBySidonmAndGugunnm(String sidonm, String gugunnm);
}
