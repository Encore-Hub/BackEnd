package com.team6.backend.like.controller;

import com.team6.backend.common.response.ResponseMessage;
import com.team6.backend.like.dto.LikeRequestDto;
import com.team6.backend.like.dto.LikeResponseDto;
import com.team6.backend.like.service.LikeService;
import com.team6.backend.pfmc.entity.Pfmc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 좋아요 등록 및 취소
    @PostMapping("/toggle")
    public ResponseEntity<ResponseMessage<LikeResponseDto>> toggleLike(@RequestBody LikeRequestDto likeRequestDto) {
        likeService.toggleLike(likeRequestDto.getMt20id(), likeRequestDto.getMemberId());
        boolean isLiked = likeService.isLiked(likeRequestDto.getMt20id(), likeRequestDto.getMemberId());
        long likeCount = likeService.getLikeCount(likeRequestDto.getMt20id());
        LikeResponseDto responseDto = new LikeResponseDto(likeRequestDto.getMt20id(), likeRequestDto.getMemberId(),isLiked, likeCount);

        return ResponseEntity.ok(new ResponseMessage<>("좋아요 상태가 변경되었습니다.", responseDto));
    }

    // 좋아요 수 조회
    @GetMapping("/{mt20id}/count")
    public ResponseEntity<ResponseMessage<Long>> getLikeCount(@PathVariable String mt20id) {
        long likeCount = likeService.getLikeCount(mt20id);
        return ResponseEntity.ok(new ResponseMessage<>("좋아요 수 조회 성공", likeCount));
    }

    // 특정 사용자가 좋아요 누른 공연 목록 조회
    @GetMapping("/mypage/performances/{memberId}")
    public ResponseEntity<ResponseMessage<List<Pfmc>>> getLikedPerformancesByMember(@PathVariable Long memberId) {
        List<Pfmc> likedPerformances = likeService.getLikedPerformancesByMember(memberId);
        return ResponseEntity.ok(new ResponseMessage<>("사용자가 좋아요 등록한 공연 목록 조회 성공", likedPerformances));
    }
}
