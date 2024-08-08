package com.team6.backend.favorite.pfmc.dto;

import lombok.Getter;
@Getter
public class FavoritePfmcResponseDto {
    private final Long id;
    private final String pfmcName; // 공연 이름
    private final String prmcPoster;
    private final String pfmcTheaterName;
    private final boolean favoritePfmc;

    public FavoritePfmcResponseDto(Long id, String pfmcName, String prmcPoster,String pfmcTheaterName , boolean favoritePfmc) {
        this.id = id;
        this.pfmcName = pfmcName;
        this.prmcPoster = prmcPoster;
        this.pfmcTheaterName = pfmcTheaterName;
        this.favoritePfmc = favoritePfmc;
    }

    // Getter methods for id, pfmcName, favoritePfmc
}


