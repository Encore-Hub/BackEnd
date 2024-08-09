package com.team6.backend.theater.theater.dto;

import com.team6.backend.pfmc.entity.Pfmc;
import com.team6.backend.theater.theater.entity.TheaterDetail;
import lombok.Getter;

import java.util.List;

@Getter
public class TheaterDetailPfmcResponseDto {
    private final String mt10id;
    private final String fcltychartr;
    private final String fcltynm;
    private final int seatscale;
    private final int mt13cnt;
    private final String telno;
    private final String relateurl;
    private final String adres;
    private final String parkinglot;
    private final List<Pfmc> performances;

    public TheaterDetailPfmcResponseDto(TheaterDetail theaterDetail, List<Pfmc> performances) {
        this.mt10id = theaterDetail.getMt10id();
        this.fcltychartr = theaterDetail.getFcltychartr();
        this.fcltynm = theaterDetail.getFcltynm();
        this.seatscale = theaterDetail.getSeatscale();
        this.mt13cnt = theaterDetail.getMt13cnt();
        this.telno = theaterDetail.getTelno();
        this.relateurl = theaterDetail.getRelateurl();
        this.adres = theaterDetail.getAdres();
        this.parkinglot = theaterDetail.getParkinglot();
        this.performances = performances;
    }
}
