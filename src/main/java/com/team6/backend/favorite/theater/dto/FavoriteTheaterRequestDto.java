    package com.team6.backend.favorite.theater.dto;

    import lombok.Getter;

    @Getter
    public class FavoriteTheaterRequestDto {
        private final Long memberId;
        private final String theaterId; // 수정된 부분

        public FavoriteTheaterRequestDto(Long memberId, String theaterId) {
            this.memberId = memberId;
            this.theaterId = theaterId;
        }
    }
