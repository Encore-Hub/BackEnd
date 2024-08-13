package com.team6.backend.favorite.theater.dto;

import lombok.Getter;

@Getter
public class FavoriteTheaterResponseDto {

    private final Long id;
    private final String theaterName;
    private final String theaterId;
    private final boolean isFavorited;

    public FavoriteTheaterResponseDto(Long id, String theaterName, String theaterId, boolean isFavorited) {
        this.id = id;
        this.theaterName = theaterName;
        this.theaterId = theaterId;
        this.isFavorited = isFavorited;
    }
}
