package com.team6.backend.favorite.pfmc.dto;

import com.team6.backend.pfmc.entity.Pfmc;
import lombok.Getter;
@Getter
public class FavoritePfmcResponseDto {

    private final Long id;
    private final String mt20id;
    private final String prfnm; // PFMC의 이름
    private final String poster;
    private final boolean isFavorited;

    public FavoritePfmcResponseDto(Long id, Pfmc pfmc, boolean isFavorited) {
        this.id = id;
        this.mt20id = pfmc.getMt20id();
        this.prfnm = pfmc.getPrfnm();
        this.poster = pfmc.getPoster();
        this.isFavorited = isFavorited;
    }

}



