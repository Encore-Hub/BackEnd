package com.team6.backend.favorite.pfmc.dto;

import com.team6.backend.pfmc.entity.Pfmc;
import lombok.Getter;
@Getter
public class FavoritePfmcResponseDto {

    private Long id;
    private String pfmcName; // PFMC의 이름
    private String prmcPoster;
    private boolean favoritePfmc;

    public FavoritePfmcResponseDto(Long id, Pfmc pfmc) {
        this.id = id;
        this.pfmcName = pfmc.getPrfnm();
        this.prmcPoster = pfmc.getPoster();
        this.favoritePfmc = favoritePfmc;
    }

    // Getter methods for id, pfmcName, prmcPoster, favoritePfmc
}

    // Getter methods for id, pfmcName, favoritePfmc



