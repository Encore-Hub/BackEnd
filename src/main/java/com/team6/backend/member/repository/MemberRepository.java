package com.team6.backend.member.repository;

import com.team6.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String username);
    Optional<Member> findByNickname(String phoneNumber);
    Optional<Member> findByPhoneNumber(String phoneNumber);
}
