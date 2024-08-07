package com.team6.backend.theater.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TheaterDetailDto {
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

    // Additional fields
    private String restaurant;
    private String cafe;
    private String store;
    private String nolibang;
    private String suyu;
    private String parkbarrier;
    private String restbarrier;
    private String runwbarrier;
    private String elevbarrier;
}
