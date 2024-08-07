    package com.team6.backend.favorite.theater.dto;

    import com.fasterxml.jackson.annotation.JsonProperty;
    import lombok.Getter;

    @Getter
    public class FavoriteTheaterRequestDto {
        private Long memberId;
        private String theaterId; // 수정된 부분
    }
