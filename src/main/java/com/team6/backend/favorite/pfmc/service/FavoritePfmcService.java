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
    public void toggleFavoritePfmc(FavoritePfmcRequestDto request) {
        log.debug("즐겨찾기 PFMC를 토글하는 요청이 수신되었습니다: {}", request);

        // 입력 값 유효성 검사
        if (request.getMemberId() == null || request.getMt20id() == null) {
            throw new EncoreHubException(ErrorCode.INVALID_INPUT_VALUE, "회원 ID 또는 PFMC ID가 null입니다.");
        }

        // ID에 따라 PFMC 찾기
        Pfmc pfmc = pfmcRepository.findById(request.getMt20id())
                .orElseThrow(() -> new EncoreHubException(ErrorCode.PFMC_NOT_FOUND, "ID " + request.getMt20id() + "에 해당하는 PFMC를 찾을 수 없습니다."));
        log.debug("PFMC 찾음: {}", pfmc);

        // ID에 따라 회원 찾기
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new EncoreHubException(ErrorCode.MEMBER_NOT_FOUND, "ID " + request.getMemberId() + "에 해당하는 회원을 찾을 수 없습니다."));
        log.debug("회원 찾음: {}", member);

        // 즐겨찾기 PFMC 항목 찾기 또는 생성Z
        List<FavoritePfmc> favoritePfmcList = favoritePfmcRepository.findByMemberAndPfmc(member, pfmc);
        FavoritePfmc favoritePfmc;
        if (favoritePfmcList.isEmpty()) {
            favoritePfmc = new FavoritePfmc(member, pfmc, false);
        } else {
            favoritePfmc = favoritePfmcList.get(0); // 하나의 항목만 있는 것으로 가정, 여러 개면 조정 가능
        }
        log.debug("즐겨찾기 PFMC 찾거나 생성함: {}", favoritePfmc);

        // 즐겨찾기 상태 토글
        favoritePfmc.toggleFavorite();
        favoritePfmcRepository.save(favoritePfmc);
        log.debug("즐겨찾기 PFMC 토글하고 저장함: {}", favoritePfmc);
    }

    @Transactional(readOnly = true)
    public List<FavoritePfmcResponseDto> getFavoritePfmcListByMemberId(Long memberId) {
        log.debug("회원 ID {}에 대한 즐겨 찾기한 PFMC 목록을 조회합니다.", memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EncoreHubException(ErrorCode.MEMBER_NOT_FOUND, "회원 ID에 해당하는 회원을 찾을 수 없습니다: " + memberId));

        List<FavoritePfmc> favoritePfmcList = favoritePfmcRepository.findByMember(member);

        return favoritePfmcList.stream()
                .map(fp -> new FavoritePfmcResponseDto(fp.getId(), fp.getPfmc().getPfmcName(),fp.getPfmc().getPfmcPoser(),fp.getPfmc().getpfmcTheaterName(), fp.isFavoritePfmc()))
                .collect(Collectors.toList());
    }


}
