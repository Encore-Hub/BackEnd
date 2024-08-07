package com.team6.backend.favorite.pfmc.dto;

import lombok.Getter;

@Getter
public class FavoritePfmcResponseDto {

    private Long id;
    private String pfmcName; // PFMC의 이름

    private boolean favoritePfmc;

    public FavoritePfmcResponseDto(Long id, String pfmcName, boolean favoritePfmc) {
        this.id = id;
        this.pfmcName = pfmcName;
        this.favoritePfmc = favoritePfmc;
    }

    // Getter methods for id, pfmcName, favoritePfmc
}
