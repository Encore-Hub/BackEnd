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
    public void toggleFavoriteTheater(String theaterId, String email) {

        log.debug("Received request to toggle favorite theater: {}", theaterId);

        // Find TheaterDetail
        TheaterDetail theaterDetail = theaterDetailRepository.findByMt10id(theaterId)
                .orElseThrow(() -> new EncoreHubException(ErrorCode.THEATER_NOT_FOUND, "Theater with ID " + theaterId + " not found"));
        log.debug("TheaterDetail found: {}", theaterDetail);

        // Find Member
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EncoreHubException(ErrorCode.MEMBER_NOT_FOUND, "Member with email " + email + " not found"));
        log.debug("Member found: {}", member);

        // Find or create FavoriteTheater
        List<FavoriteTheater> favoriteTheaterList = favoriteTheaterRepository.findByMemberAndTheaterDetail(member, theaterDetail);
        FavoriteTheater favoriteTheater;
        if (favoriteTheaterList.isEmpty()) {
            favoriteTheater = new FavoriteTheater(member, theaterDetail, false);
        } else {
            favoriteTheater = favoriteTheaterList.get(0); // Assume single entry
        }
        log.debug("Found or created FavoriteTheater: {}", favoriteTheater);

        // Toggle favorite status
        favoriteTheater.toggleFavorite();
        favoriteTheaterRepository.save(favoriteTheater);
        log.debug("Saved FavoriteTheater: {}", favoriteTheater);
    }

    @Transactional(readOnly = true)
    public List<FavoriteTheaterResponseDto> getAllFavoriteTheatersByEmail(String email) {
        log.debug("Fetching favorite theaters for email {}", email);

        // Find member by email
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EncoreHubException(ErrorCode.MEMBER_NOT_FOUND, "Member not found for email " + email));

        // Find favorite theaters for the member
        List<FavoriteTheater> favoriteTheaters = favoriteTheaterRepository.findByMember(member);

        // Map to response DTOs
        return favoriteTheaters.stream()
                .map(ft -> new FavoriteTheaterResponseDto(
                        ft.getId(),
                        ft.getTheaterDetail().getFcltynm(), // Theater name
                        ft.getTheaterDetail().getMt10id(), // Theater ID
                        ft.isFavorited()
                ))


                .collect(Collectors.toList());
    }
}
