package com.team6.backend.theater.theater.repository;


import com.team6.backend.theater.theater.entity.TheaterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterIdRepository extends JpaRepository<TheaterId, Long> {
    List<TheaterId> findBySidonmAndGugunnm(String sidonm, String gugunnm);
}
