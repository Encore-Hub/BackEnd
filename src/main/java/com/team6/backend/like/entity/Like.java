package com.team6.backend.like.entity;

import com.team6.backend.member.entity.Member;
import com.team6.backend.pfmc.entity.Pfmc;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "pfmc_id", nullable = false)
    private Pfmc pfmc;

    private boolean liked = true;  // 기본값을 true로 설정

    public Like(Member member, Pfmc pfmc) {
        this.member = member;
        this.pfmc = pfmc;
    }

}
