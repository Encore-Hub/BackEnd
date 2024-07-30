package com.team6.backend.members.repository;

import com.team6.backend.members.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByEmail(String username);
    Optional<Member> findByPhoneNumber(String phoneNumber);
    Optional<Member> findByNickname(String phoneNumber);
}
