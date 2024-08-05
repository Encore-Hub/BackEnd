package com.team6.backend.Pfmc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Table(name = "pfmclist")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PfmcList {

    @Id
    @Column(name = "pfmc_id")
    private String mt20id; // 공연id
    @Column(name = "pfmcname")
    private String prfnm;   // 공연명
    @Column(name = "pfcmstartday")
    private String prfpdfrom; // 공연시작일
    @Column(name = "pfcmendday")
    private String prfpdto; // 공연 종료일
    @Column(name = "stage")
    private String fcltynm; // 공연시설명
    @Column(name = "poster")
    private String poster;  // 공연포스터 경로
    @Column(name = "pfmcstate")
    private String prfstate;// 공연 상태
    public String getPfmcId() {
        return mt20id;
    }
}
