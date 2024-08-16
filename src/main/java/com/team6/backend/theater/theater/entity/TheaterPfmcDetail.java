package com.team6.backend.theater.theater.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "theater_pfmc")
public class TheaterPfmcDetail {
    @Id
    private String mt10id;

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
