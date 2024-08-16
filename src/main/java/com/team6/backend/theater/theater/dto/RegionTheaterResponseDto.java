package com.team6.backend.theater.theater.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegionTheaterResponseDto {
    private String mt10id;
    private String fcltynm;
    private String adres;
    private double la;
    private double lo;
}
