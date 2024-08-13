package com.team6.backend.favorite.theater.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteTheaterToggleResponseDto {
    private String message;
    private boolean isFavorited;
}
