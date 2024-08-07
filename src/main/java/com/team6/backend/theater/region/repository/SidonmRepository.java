package com.team6.backend.theater.region.repository;


import com.team6.backend.theater.region.entity.Sidonm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SidonmRepository extends JpaRepository<Sidonm, Long> {
    Optional<Sidonm> findBySidonm(String sidonm);
}