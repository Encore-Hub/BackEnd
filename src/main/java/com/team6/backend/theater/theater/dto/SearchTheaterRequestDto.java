package com.team6.backend.theater.theater.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchTheaterRequestDto {
    private String theaterName; // 공연장 이름 필드 추가
}
