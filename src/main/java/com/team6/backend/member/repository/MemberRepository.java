package com.team6.backend.member.repository;

import com.team6.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String Nickname);

    Optional<Member> findByPhoneNumber(String phoneNumber);

    Optional<Member> findByKakaoId(Long kakaoId);
}
