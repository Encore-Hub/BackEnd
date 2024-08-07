package com.team6.backend.theater.region.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RegionResponseDto {
    private String sidonm;
    private List<String> gugunnms;
}
