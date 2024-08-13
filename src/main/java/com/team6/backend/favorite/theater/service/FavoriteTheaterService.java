package com.team6.backend.favorite.theater.service;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.common.exception.ErrorCode;
import com.team6.backend.favorite.theater.dto.FavoriteTheaterRequestDto;
import com.team6.backend.favorite.theater.dto.FavoriteTheaterResponseDto;
import com.team6.backend.favorite.theater.dto.FavoriteTheaterToggleResponseDto;
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
    public boolean toggleFavoriteTheater(String theaterId, String email) {
        log.debug("Toggling favorite Theater for theater ID {} and email {}", theaterId, email);

        // Find TheaterDetail by ID
        TheaterDetail theaterDetail = theaterDetailRepository.findByMt10id(theaterId)
                .orElseThrow(() -> new EncoreHubException(ErrorCode.THEATER_NOT_FOUND, "Theater with ID " + theaterId + " not found"));
        log.debug("Found TheaterDetail: {}", theaterDetail);

        // Find Member by email
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EncoreHubException(ErrorCode.MEMBER_NOT_FOUND, "Member with email " + email + " not found"));
        log.debug("Found Member: {}", member);

        // Find FavoriteTheater
        List<FavoriteTheater> favoriteTheaterList = favoriteTheaterRepository.findByMemberAndTheaterDetail(member, theaterDetail);
        boolean isFavorited;

        if (favoriteTheaterList.isEmpty()) {
            // No favorite exists, create a new one
            FavoriteTheater newFavoriteTheater = new FavoriteTheater(member, theaterDetail, true);
            favoriteTheaterRepository.save(newFavoriteTheater);
            log.debug("Created new FavoriteTheater: {}", newFavoriteTheater);
            isFavorited = true; // Newly favorited
        } else {
            // Favorite exists, toggle its status
            FavoriteTheater favoriteTheater = favoriteTheaterList.get(0); // Assuming a single entry
            favoriteTheater.toggleFavorite();
            if (!favoriteTheater.isFavorited()) {
                // Status is false, delete from repository
                favoriteTheaterRepository.delete(favoriteTheater);
                log.debug("Deleted FavoriteTheater: {}", favoriteTheater);
                isFavorited = false; // Unfavorited
            } else {
                // Status is true, save updated entity
                favoriteTheaterRepository.save(favoriteTheater);
                log.debug("Updated FavoriteTheater: {}", favoriteTheater);
                isFavorited = true; // Still favorited
            }
        }

        return isFavorited;
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
