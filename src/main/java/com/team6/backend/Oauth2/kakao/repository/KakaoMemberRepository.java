package com.team6.backend.Oauth2.kakao.repository;

import com.team6.backend.Oauth2.kakao.entity.KakaoMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoMemberRepository extends JpaRepository<KakaoMember, Long> {
}
