package com.team6.backend.theater.theater.service;

import com.team6.backend.common.exception.ErrorCode;
import com.team6.backend.common.exception.TheaterException;
import com.team6.backend.pfmc.entity.Pfmc;
import com.team6.backend.pfmc.repository.PfmcRepository;
import com.team6.backend.theater.api.dto.TheaterDetailResponseDto;
import com.team6.backend.theater.api.dto.TheaterDto;
import com.team6.backend.theater.theater.dto.*;
import com.team6.backend.theater.theater.entity.TheaterDetail;
import com.team6.backend.theater.theater.entity.TheaterId;
import com.team6.backend.theater.theater.repository.TheaterDetailRepository;
import com.team6.backend.theater.theater.repository.TheaterIdRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TheaterService {
    private final TheaterDetailRepository theaterDetailRepository;
    private final TheaterIdRepository theaterIdRepository;
    private PfmcRepository pfmcRepository;

    public TheaterService(TheaterDetailRepository theaterDetailRepository, TheaterIdRepository theaterIdRepository, PfmcRepository pfmcRepository) {
        this.theaterDetailRepository = theaterDetailRepository;
        this.theaterIdRepository = theaterIdRepository;
        this.pfmcRepository = pfmcRepository;
    }

    // 지역별 공연장 리스트 조회
    public List<RegionTheaterResponseDto> getRegionTheaterList(RegionTheaterRequestDto regionTheaterRequestDto) {
        String sidonm = regionTheaterRequestDto.getSidonm();
        String gugunnm = regionTheaterRequestDto.getGugunnm();

        // DB에서 공연장 리스트 검색
        List<TheaterId> theaterIds = theaterIdRepository.findBySidonmAndGugunnm(sidonm, gugunnm);

        // 공연장 리스트에서 "폐관" 상태의 공연장을 필터링하고 DTO로 변환
        return theaterIds.stream()
                .filter(theater -> !theater.getFcltynm().contains("폐관"))
                .map(theater -> new RegionTheaterResponseDto(theater.getMt10id(), theater.getFcltynm()))
                .collect(Collectors.toList());
    }

    // 공연장 이름으로 검색
    public SearchTheaterResponseDto searchTheater(SearchTheaterRequestDto searchTheaterRequestDto) {
        String theaterName = searchTheaterRequestDto.getTheaterName();

        // DB에서 공연장 리스트 검색
        List<TheaterDetail> theaterDetails = theaterDetailRepository.findByFcltynmContaining(theaterName);

        if (theaterDetails.isEmpty()) {
            throw new TheaterException(ErrorCode.THEATER_NOT_FOUND);
        }

        // 검색 결과에서 "폐관" 상태의 공연장을 필터링하고 DTO로 변환
        List<TheaterDto> theaterDtoList = theaterDetails.stream()
                .filter(theater -> !theater.getFcltynm().contains("폐관"))
                .map(theater -> new TheaterDto(
                        theater.getMt10id(),
                        theater.getSidonm(),
                        theater.getGugunnm(),
                        theater.getFcltynm()
                ))
                .collect(Collectors.toList());

        return SearchTheaterResponseDto.builder()
                .theaters(theaterDtoList)
                .build();
    }

    public TheaterDetailPfmcResponseDto getTheaterDetailWithPerformances(String mt10id) {
        // Retrieve TheaterDetail
        TheaterDetail theaterDetail = theaterDetailRepository.findByMt10id(mt10id)
                .orElseThrow(() -> new TheaterException(ErrorCode.THEATER_DETAIL_NOT_FOUND));

        // Retrieve performances associated with mt10id
        List<Pfmc> performances = pfmcRepository.findByMt10id(mt10id);

        // Create TheaterDetailPfmcResponseDto with both TheaterDetail and performances
        return new TheaterDetailPfmcResponseDto(theaterDetail, performances);
    }
}
