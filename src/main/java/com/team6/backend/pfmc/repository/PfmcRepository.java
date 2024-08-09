package com.team6.backend.pfmc.repository;


import com.team6.backend.pfmc.entity.Pfmc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PfmcRepository extends JpaRepository<Pfmc, String> {
    List<Pfmc> findByMt10id(String mt10id);
    // 메서드 정의
}

