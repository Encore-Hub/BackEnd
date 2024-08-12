package com.team6.backend.favorite.theater.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteTheaterResponseDto {

    private Long id;
    private String theaterName;
    private String theaterId;
    private boolean favoriteTheater;

    // Manually defined constructor
    public FavoriteTheaterResponseDto(Long id, String theaterName,String theaterId, boolean favoriteTheater) {
        this.id = id;
        this.theaterName = theaterName;
        this.theaterId = theaterId;
        this.favoriteTheater = favoriteTheater;
    }
}
