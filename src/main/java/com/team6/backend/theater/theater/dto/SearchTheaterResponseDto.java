package com.team6.backend.theater.theater.dto;

import com.team6.backend.theater.api.dto.TheaterDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchTheaterResponseDto {
    private List<TheaterDto> theaters;
    private String message; // 메시지 필드 추가
}
