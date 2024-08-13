package com.team6.backend.like.service;

import com.team6.backend.common.exception.ResourceNotFoundException;
import com.team6.backend.like.dto.LikedPfmcResponseDto;
import com.team6.backend.like.entity.Like;
import com.team6.backend.like.repository.LikeRepository;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import com.team6.backend.pfmc.entity.Pfmc;
import com.team6.backend.pfmc.repository.PfmcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PfmcRepository pfmcRepository;
    private final MemberRepository memberRepository;

    // 좋아요 추가 또는 취소
    @Transactional
    public void toggleLike(String mt20id, String email) {
        Pfmc pfmc = pfmcRepository.findById(mt20id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Like like = likeRepository.findByMemberAndPfmc(member, pfmc)
                .orElseGet(() -> new Like(member, pfmc, false)); // 없을 경우 새로 생성

        like.toggleLiked(); // 상태 변경
        likeRepository.save(like);
    }

    // 특정 공연의 좋아요 수 조회
    public long getLikeCount(String mt20id) {
        Pfmc pfmc = pfmcRepository.findById(mt20id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        return likeRepository.countByPfmcAndLiked(pfmc, true);
    }

    public List<LikedPfmcResponseDto> getLikedPerformancesByMember(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Like> likes = likeRepository.findByMember(member);
        if (likes == null || likes.isEmpty()) {
            return Collections.emptyList();
        }

        return likes.stream()
                .map(like -> {
                    Pfmc pfmc = like.getPfmc();
                    return new LikedPfmcResponseDto(
                            pfmc.getMt20id(),
                            pfmc.getPrfnm(),
                            pfmc.getPoster(),
                            like.isLiked()  // 좋아요 상태
                    );
                })
                .collect(Collectors.toList());
    }


    // 특정 공연에 사용자가 좋아요를 눌렀는지 확인
    public boolean isLiked(String mt20id, String email) {
        Pfmc pfmc = pfmcRepository.findById(mt20id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return likeRepository.findByMemberAndPfmc(member, pfmc)
                .map(Like::isLiked)
                .orElse(false);
    }
}
