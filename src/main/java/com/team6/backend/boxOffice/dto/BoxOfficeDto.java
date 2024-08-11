package com.team6.backend.boxOffice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoxOfficeDto {
    private String prfnm;    // 공연명
    private String mt20id;   // 공연ID
    private String prfplcnm; // 공연장소명
    private String prfpd;    // 공연 기간
    private String rnum;     // 순위
    private String poster;   // 포스터 이미지 URL
}
