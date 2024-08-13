package com.team6.backend.favorite.pfmc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoritePfmcToggleResponseDto {
    private String message;
    private boolean isFavorited;
}
