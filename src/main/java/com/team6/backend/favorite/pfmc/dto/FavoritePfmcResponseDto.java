package com.team6.backend.favorite.pfmc.dto;

import lombok.Getter;
@Getter
public class FavoritePfmcResponseDto {
    private Long id;
    private String pfmcName; // 공연 이름
    private String prmcPoster;
    private String pfmcTheaterName;
    private boolean favoritePfmc;

    public FavoritePfmcResponseDto(Long id, String pfmcName, String prmcPoster,String pfmcTheaterName , boolean favoritePfmc) {
        this.id = id;
        this.pfmcName = pfmcName;
        this.prmcPoster = prmcPoster;
        this.pfmcTheaterName = pfmcTheaterName;
        this.favoritePfmc = favoritePfmc;
    }

    // Getter methods for id, pfmcName, favoritePfmc
}


