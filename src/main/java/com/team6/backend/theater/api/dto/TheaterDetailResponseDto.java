package com.team6.backend.theater.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TheaterDetailResponseDto {
    private String mt10id;
    private String fcltychartr;
    private String sidonm;
    private String gugunnm;
    private String fcltynm;
    private int seatscale;
    private int mt13cnt;
    private String telno;
    private String relateurl;
    private String adres;
    private double la;
    private double lo;
    private String parkinglot;
}
