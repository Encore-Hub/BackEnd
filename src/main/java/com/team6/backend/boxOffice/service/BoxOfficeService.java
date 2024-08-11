package com.team6.backend.boxOffice.service;

import com.team6.backend.boxOffice.dto.BoxOfficeDto;
import com.team6.backend.boxOffice.entity.BoxOffice;
import com.team6.backend.boxOffice.repository.BoxOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoxOfficeService {
    private final BoxOfficeRepository boxOfficeRepository;

    @Autowired
    public BoxOfficeService(BoxOfficeRepository boxOfficeRepository) {
        this.boxOfficeRepository = boxOfficeRepository;
    }

    public List<BoxOfficeDto> getAllBoxOfficeData() {
        List<BoxOffice> boxOfficeEntities = boxOfficeRepository.findAll();
        return boxOfficeEntities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BoxOfficeDto convertToDto(BoxOffice boxOffice) {
        return new BoxOfficeDto(
                boxOffice.getPrfnm(),
                boxOffice.getMt20id(),
                boxOffice.getPrfplcnm(),
                boxOffice.getPrfpd(),
                boxOffice.getRnum(),
                boxOffice.getPoster()
        );
    }
}
