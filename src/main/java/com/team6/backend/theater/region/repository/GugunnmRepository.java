package com.team6.backend.theater.region.repository;

import com.team6.backend.theater.region.entity.Gugunnm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GugunnmRepository extends JpaRepository<Gugunnm, String> {
    List<Gugunnm> findBySidonm(String sidonm);
}