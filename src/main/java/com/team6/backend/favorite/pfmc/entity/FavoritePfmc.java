package com.team6.backend.favorite.pfmc.entity;

import com.team6.backend.member.entity.Member;
import com.team6.backend.pfmc.entity.Pfmc;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@NoArgsConstructor
public class FavoritePfmc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pfmc_id")
    private Pfmc pfmc;
    
    @ColumnDefault("false")
    private boolean favoritePfmc = false;

    public FavoritePfmc(Member member, Pfmc pfmc, boolean favoritePfmc) {
        this.member = member;
        this.pfmc = pfmc;
        this.favoritePfmc = favoritePfmc;
    }

    public void toggleFavorite() {
        this.favoritePfmc = !this.favoritePfmc;
    }
}
