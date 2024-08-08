package com.team6.backend.favorite.pfmc.dto;

import com.team6.backend.pfmc.entity.Pfmc;
import lombok.Getter;
@Getter
public class FavoritePfmcResponseDto {

    private final Long id;
    private final String pfmcName; // PFMC의 이름
    private final String prmcPoster;
    private boolean favoritePfmc;

    public FavoritePfmcResponseDto(Long id, Pfmc pfmc, boolean favoritePfmc) {
        this.id = id;
        this.pfmcName = pfmc.getPrfnm();
        this.prmcPoster = pfmc.getPoster();
        this.favoritePfmc = favoritePfmc;
    }

    // Getter methods for id, pfmcName, prmcPoster, favoritePfmc
}

    // Getter methods for id, pfmcName, favoritePfmc



