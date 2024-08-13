package com.team6.backend.theater.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TheaterPfmcDetailDto {
    private String fcltychartr;
    private String sidonm;
    private String gugunnm;
    private String fcltynm;
    private int seatscale;
    private int mt13cnt;
    private String telno;
    private String adres;
    private double la;
    private double lo;
    private String parkinglot;
}
