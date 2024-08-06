package com.team6.backend.like.service;

import com.team6.backend.common.exception.ResourceNotFoundException;
import com.team6.backend.like.entity.Like;
import com.team6.backend.like.repository.LikeRepository;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import com.team6.backend.pfmc.entity.Pfmc;
import com.team6.backend.pfmc.repository.PfmcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void toggleLike(String mt20id, Long memberId) {
        Pfmc pfmc = pfmcRepository.findById(mt20id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        Member member = memberRepository.findById(memberId)
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

        return likeRepository.countByPrmcAndLiked(pfmc, true);
    }

    public List<Pfmc> getLikedPerformancesByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 사용자가 좋아요 누른 공연만 필터링하여 리스트 반환
        return likeRepository.findByMember(member).stream()
                .filter(Like::isLiked)
                .map(Like::getPfmc)
                .collect(Collectors.toList());
    }


    // 특정 공연에 사용자가 좋아요를 눌렀는지 확인
    public boolean isLiked(String mt20id, Long memberId) {
        Pfmc pfmc = pfmcRepository.findById(mt20id)
                .orElseThrow(() -> new ResourceNotFoundException("Performance not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return likeRepository.findByMemberAndPfmc(member, pfmc)
                .map(Like::isLiked)
                .orElse(false);
    }
}
