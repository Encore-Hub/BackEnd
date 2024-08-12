package com.team6.backend.boxOffice.repository;

import com.team6.backend.boxOffice.entity.BoxOffice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoxOfficeRepository extends JpaRepository<BoxOffice, Long> {
}