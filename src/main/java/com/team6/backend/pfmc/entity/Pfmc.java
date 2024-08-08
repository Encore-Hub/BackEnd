package com.team6.backend.pfmc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Pfmc {

    private String prfpdfrom;
    private String fcltynm;
    private String entrpsnmH;
    private String entrpsnmA;
    private String prfpdto;
    private String pcseguidance;
    private String dtguidance;
    private String prfcrew;
    private String prfage;
    private String prfstate;
    private String updatedate;
    private String entrpsnm;
    private String mt10id;
    private String musicallicense;
    private String area;
    private String festival;
    private String musicalcreate;
    private String prfcast;
    private String prfruntime;
    private String openrun;
    private String daehakro;
    private String entrpsnmS;
    @Id
    private String mt20id;
    private String entrpsnmP;
    private String visit;
    private String prfnm;
    private String genrenm;
    private String poster;
    private String child;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pfmc_id")
    private List<RelateInfo> relateInfos = new ArrayList<>();

    @ElementCollection
    private List<String> styurls = new ArrayList<>();

    public void addRelateInfo(RelateInfo relateInfo) {
        relateInfos.add(relateInfo);
    }

    public void addStyurl(String styurl) {
        styurls.add(styurl);
    }


}
