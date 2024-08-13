package com.team6.backend.theater.theater.repository;


import com.team6.backend.theater.theater.entity.TheaterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheaterIdRepository extends JpaRepository<TheaterId, Long> {
    Optional<TheaterId> findByMt10id(String mt10id);
}
