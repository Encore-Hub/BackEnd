package com.team6.backend.theater.api.dto;

import lombok.Getter;

@Getter
public class TheaterIdDto {
    // 게터 메서드들
    private String mt10id;
    private String fcltynm;
    private String fcltychartr;


    // 생성자
    public TheaterIdDto(String mt10id, String fcltynm, String fcltychartr) {
        this.mt10id = mt10id;
        this.fcltynm = fcltynm;
        this.fcltychartr = fcltychartr;
    }

    @Override
    public String toString() {
        return "TheaterDto{" +
                "mt10id='" + mt10id + '\'' +
                ", fcltynm='" + fcltynm + '\'' +
                ", fcltychartr='" + fcltychartr + '\'' +
                '}';
    }
}
