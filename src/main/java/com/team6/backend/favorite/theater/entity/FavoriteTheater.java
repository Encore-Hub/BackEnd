package com.team6.backend.favorite.theater.entity;

import com.team6.backend.member.entity.Member;
import com.team6.backend.theater.theater.entity.TheaterDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class FavoriteTheater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_mt10id")
    private TheaterDetail theaterDetail;

    private boolean favoriteTheater;

    public FavoriteTheater(Member member, TheaterDetail theaterDetail, boolean favoriteTheater) {
        this.member = member;
        this.theaterDetail = theaterDetail;
        this.favoriteTheater = favoriteTheater;
    }

    public void toggleFavorite() {
        this.favoriteTheater = !this.favoriteTheater;
    }
}
