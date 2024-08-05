package com.team6.backend.pfmc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class RelateInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String relatenm;
    private String relateurl;

    public RelateInfo(String relatenm, String relateurl) {
        this.relatenm = relatenm;
        this.relateurl = relateurl;
    }
}