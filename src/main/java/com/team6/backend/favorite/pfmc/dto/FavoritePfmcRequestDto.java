package com.team6.backend.favorite.pfmc.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FavoritePfmcRequestDto {

    private Long memberId;

    @JsonProperty("mt20id")
    private String mt20id;

    // Constructors
    public FavoritePfmcRequestDto() {
    }

    public FavoritePfmcRequestDto(Long memberId, String mt20id) {
        this.memberId = memberId;
        this.mt20id = mt20id;
    }


}
