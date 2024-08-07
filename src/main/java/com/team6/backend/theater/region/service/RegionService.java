package com.team6.backend.theater.region.service;

import com.team6.backend.common.exception.ErrorCode;
import com.team6.backend.common.exception.TheaterException;
import com.team6.backend.theater.region.dto.RegionRequestDto;
import com.team6.backend.theater.region.dto.RegionResponseDto;
import com.team6.backend.theater.region.entity.Gugunnm;
import com.team6.backend.theater.region.repository.GugunnmRepository;
import com.team6.backend.theater.region.repository.SidonmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionService {
    private static final Logger logger = LoggerFactory.getLogger(RegionService.class);
    private final SidonmRepository sidonmRepository;
    private final GugunnmRepository gugunnmRepository;

    public RegionService(SidonmRepository sidonmRepository, GugunnmRepository gugunnmRepository) {
        this.sidonmRepository = sidonmRepository;
        this.gugunnmRepository = gugunnmRepository;
    }

    public RegionResponseDto getRegionGugunnm(RegionRequestDto regionRequestDto) {
        String sidonm = regionRequestDto.getSidonm();
        logger.info("Requested Sidonm: {}", sidonm);

        // Sidonm 검증
        if (sidonm == null || sidonm.isEmpty()) {
            logger.error("Sidonm is null or empty.");
            throw new TheaterException(ErrorCode.INVALID_THEATER_DATA);
        }

        if (sidonmRepository.findBySidonm(sidonm).isEmpty()) {
            logger.error("Sidonm '{}' not found in the database.", sidonm);
            throw new TheaterException(ErrorCode.SIDONM_NOT_FOUND);
        }

        // Gugunnm 조회
        List<Gugunnm> gugunnms = gugunnmRepository.findBySidonm(sidonm);
        if (gugunnms.isEmpty()) {
            logger.error("No Gugunnm found for Sidonm '{}'", sidonm);
            throw new TheaterException(ErrorCode.GUGUNNM_NOT_FOUND);
        }

        // Gugunnm 리스트를 반환
        List<String> gugunnmNames = gugunnms.stream()
                .map(Gugunnm::getGugunnm)
                .collect(Collectors.toList());

        return new RegionResponseDto(sidonm, gugunnmNames);
    }
}
