package com.team6.backend.favorite.theater.dto;

import lombok.Getter;

@Getter
public class FavoriteTheaterResponseDto {

    private final Long id;
    private final String fcltynm;
    private final String mt10id;
    private final boolean isFavorited;

    public FavoriteTheaterResponseDto(Long id, String fcltynm, String mt10id, boolean isFavorited) {
        this.id = id;
        this.fcltynm = fcltynm;
        this.mt10id = mt10id;
        this.isFavorited = isFavorited;

    }
}
