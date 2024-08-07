package com.team6.backend.theater.theater.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "theater_id")
public class TheaterId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mt10id;
    private String fcltynm;
    private String fcltychartr;
    private String sidonm;
    private String gugunnm;
    public TheaterId(String mt10id, String fcltynm, String fcltychartr, String sidonm, String gugunnm) {
        this.mt10id = mt10id;
        this.fcltynm = fcltynm;
        this.fcltychartr = fcltychartr;
        this.sidonm = sidonm;
        this.gugunnm = gugunnm;
    }
}
