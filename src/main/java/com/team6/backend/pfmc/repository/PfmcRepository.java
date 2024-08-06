package com.team6.backend.pfmc.repository;


import com.team6.backend.pfmc.entity.Pfmc;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.lang.model.element.NestingKind;

public interface PfmcRepository extends JpaRepository<Pfmc, String> {
    // 메서드 정의
}

