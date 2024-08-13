package com.team6.backend.favorite.pfmc.dto;

import com.team6.backend.pfmc.entity.Pfmc;
import lombok.Getter;
@Getter
public class FavoritePfmcResponseDto {

    private final Long id;
    private final String performanceId;
    private final String performanceName; // PFMC의 이름
    private final String performancePoster;
    private boolean isFavorited;

    public FavoritePfmcResponseDto(Long id, Pfmc pfmc, boolean isFavorited) {
        this.id = id;
        this.performanceId = pfmc.getMt20id();
        this.performanceName = pfmc.getPrfnm();
        this.performancePoster = pfmc.getPoster();
        this.isFavorited = isFavorited;
    }

}



