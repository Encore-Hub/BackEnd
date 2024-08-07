package com.team6.backend.favorite.theater.service;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.common.exception.ErrorCode;
import com.team6.backend.favorite.theater.dto.FavoriteTheaterRequestDto;
import com.team6.backend.favorite.theater.dto.FavoriteTheaterResponseDto;
import com.team6.backend.favorite.theater.entity.FavoriteTheater;
import com.team6.backend.favorite.theater.repository.FavoriteTheaterRepository;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import com.team6.backend.theater.theater.entity.TheaterDetail;
import com.team6.backend.theater.theater.repository.TheaterDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FavoriteTheaterService {

    @Autowired
    private FavoriteTheaterRepository favoriteTheaterRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TheaterDetailRepository theaterDetailRepository;

    @Transactional
    public void toggleFavoriteTheater(FavoriteTheaterRequestDto request) {
        log.debug("Received request to toggle favorite theater: {}", request);

        // Validate input values
        if (request.getTheaterId() == null || request.getMemberId() == null) {
            throw new EncoreHubException(ErrorCode.INVALID_INPUT_VALUE, "Theater ID or Member ID is null");
        }

        // Find TheaterDetail
        TheaterDetail theaterDetail = theaterDetailRepository.findByMt10id(request.getTheaterId())
                .orElseThrow(() -> new EncoreHubException(ErrorCode.THEATER_NOT_FOUND, "Theater with ID " + request.getTheaterId() + " not found"));
        log.debug("TheaterDetail found: {}", theaterDetail);

        // Find Member
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new EncoreHubException(ErrorCode.MEMBER_NOT_FOUND, "Member with ID " + request.getMemberId() + " not found"));
        log.debug("Member found: {}", member);

        // Find or create FavoriteTheater entry
        List<FavoriteTheater> favoriteTheaters = favoriteTheaterRepository.findByMemberAndTheaterDetail(member, theaterDetail);
        FavoriteTheater favoriteTheater;
        if (favoriteTheaters.isEmpty()) {
            favoriteTheater = new FavoriteTheater(member, theaterDetail, false);
        } else {
            favoriteTheater = favoriteTheaters.get(0); // Assuming one entry, can adjust if multiple
        }
        log.debug("FavoriteTheater found or created: {}", favoriteTheater);

        // Toggle favorite status
        favoriteTheater.toggleFavorite();
        favoriteTheaterRepository.save(favoriteTheater);
        log.debug("FavoriteTheater toggled and saved: {}", favoriteTheater);
    }

    @Transactional(readOnly = true)
    public List<FavoriteTheaterResponseDto> getAllFavoriteTheatersByMemberId(Long memberId) {
        log.debug("Fetching all favorite theaters for member with ID: {}", memberId);

        // Find Member
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EncoreHubException(ErrorCode.MEMBER_NOT_FOUND, "Member with ID " + memberId + " not found"));
        log.debug("Member found: {}", member);

        // Find FavoriteTheater
        List<FavoriteTheater> favoriteTheaters = favoriteTheaterRepository.findByMember(member);
        log.debug("Favorite theaters found: {}", favoriteTheaters);

        // Convert to FavoriteTheaterResponseDto list
        List<FavoriteTheaterResponseDto> responseDtoList = favoriteTheaters.stream()
                .map(ft -> new FavoriteTheaterResponseDto(ft.getId(), ft.getTheaterDetail().getName(), ft.isFavoriteTheater()))
                .collect(Collectors.toList());

        return responseDtoList;
    }

}
