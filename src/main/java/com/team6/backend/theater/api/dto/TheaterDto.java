package com.team6.backend.theater.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheaterDto {
    private String mt10id;
    private String sidonm;
    private String gugunnm;
    private String fcltynm;

}
