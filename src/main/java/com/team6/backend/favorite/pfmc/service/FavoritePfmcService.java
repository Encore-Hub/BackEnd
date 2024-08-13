package com.team6.backend.favorite.pfmc.service;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.common.exception.ErrorCode;
import com.team6.backend.favorite.pfmc.dto.FavoritePfmcRequestDto;
import com.team6.backend.favorite.pfmc.dto.FavoritePfmcResponseDto;
import com.team6.backend.favorite.pfmc.entity.FavoritePfmc;
import com.team6.backend.favorite.pfmc.repository.FavoritePfmcRepository;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import com.team6.backend.pfmc.entity.Pfmc;
import com.team6.backend.pfmc.repository.PfmcRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FavoritePfmcService {

    @Autowired
    private FavoritePfmcRepository favoritePfmcRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PfmcRepository pfmcRepository;

    @Transactional
    public boolean toggleFavoritePfmc(String performanceId, String email) {
        log.debug("Toggling favorite PFMC for performance ID {} and email {}", performanceId, email);

        // Find PFMC by performance ID
        Pfmc pfmc = pfmcRepository.findById(performanceId)
                .orElseThrow(() -> new EncoreHubException(ErrorCode.PFMC_NOT_FOUND, "PFMC not found for ID " + performanceId));
        log.debug("Found PFMC: {}", pfmc);

        // Find member by email
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EncoreHubException(ErrorCode.MEMBER_NOT_FOUND, "Member not found for email " + email));
        log.debug("Found member: {}", member);

        // Find FavoritePfmc
        List<FavoritePfmc> favoritePfmcList = favoritePfmcRepository.findByMemberAndPfmc(member, pfmc);

        if (favoritePfmcList.isEmpty()) {
            // No favorite exists, create a new one
            FavoritePfmc newFavoritePfmc = new FavoritePfmc(member, pfmc, true);
            favoritePfmcRepository.save(newFavoritePfmc);
            log.debug("Created new FavoritePfmc: {}", newFavoritePfmc);
            return true; // Newly favorited
        } else {
            // Favorite exists, toggle its status
            FavoritePfmc favoritePfmc = favoritePfmcList.get(0); // Assuming a single entry
            favoritePfmc.toggleFavorite();

            if (!favoritePfmc.isFavorited()) {
                // Status is false, delete from repository
                favoritePfmcRepository.delete(favoritePfmc);
                log.debug("Deleted FavoritePfmc: {}", favoritePfmc);
                return false; // Unfavorited
            } else {
                // Status is true, save updated entity
                favoritePfmcRepository.save(favoritePfmc);
                log.debug("Updated FavoritePfmc: {}", favoritePfmc);
                return true; // Still favorited
            }
        }
    }

    @Transactional(readOnly = true)
    public List<FavoritePfmcResponseDto> getFavoritePfmcListByEmail(String email) {
        log.debug("Fetching favorite PFMC list for email {}", email);

        // Find member by email
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EncoreHubException(ErrorCode.MEMBER_NOT_FOUND, "Member not found for email " + email));

        // Find favorites for member
        List<FavoritePfmc> favoritePfmcList = favoritePfmcRepository.findByMember(member);

        // Map to response DTOs
        return favoritePfmcList.stream()
                .map(fp -> new FavoritePfmcResponseDto(fp.getId(), fp.getPfmc(), fp.isFavorited()))
                .collect(Collectors.toList());
    }
}
