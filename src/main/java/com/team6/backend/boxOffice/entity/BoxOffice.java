package com.team6.backend.boxOffice.entity;

import com.team6.backend.boxOffice.dto.BoxOfficeDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class BoxOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String prfnm;
    private String mt20id;
    private String prfplcnm;
    private String prfpd;
    private String rnum;
    private String poster;

    // 기본 생성자
    protected BoxOffice() {
    }

    // 모든 필드를 포함한 생성자
    public BoxOffice(String prfnm, String mt20id, String prfplcnm, String prfpd, String rnum, String poster) {
        this.prfnm = prfnm;
        this.mt20id = mt20id;
        this.prfplcnm = prfplcnm;
        this.prfpd = prfpd;
        this.rnum = rnum;
        this.poster = poster;
    }

    public static BoxOffice fromDto(BoxOfficeDto dto) {
        return new BoxOffice(dto.getPrfnm(), dto.getMt20id(), dto.getPrfplcnm(), dto.getPrfpd(), dto.getRnum(), dto.getPoster());
    }

    public BoxOfficeDto toDto() {
        return new BoxOfficeDto(prfnm, mt20id, prfplcnm, prfpd, rnum, poster);
    }
}
