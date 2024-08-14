package com.team6.backend.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "member")
@Entity
@Builder
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column()
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private MemberRoleEnum role;

    private Long kakaoId;

    public Member(String username, String password, String email, MemberRoleEnum role, Long kakaoId) {
        this.nickname = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId =kakaoId;
    }

    public Member kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }
}