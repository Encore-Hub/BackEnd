package com.team6.backend.favorite.theater.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteTheaterResponseDto {

    private Long id;
    private String theaterName; // 극장 이름 등 필요한 필드 추가

    private boolean favoriteTheater;

    // 기본 생성자
    public FavoriteTheaterResponseDto() {
    }

    // 생성자
    public FavoriteTheaterResponseDto(Long id, String theaterName, boolean favoriteTheater) {
        this.id = id;
        this.theaterName = theaterName;
        this.favoriteTheater = favoriteTheater;
    }
}
