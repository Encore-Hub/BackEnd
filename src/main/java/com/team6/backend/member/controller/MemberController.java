package com.team6.backend.member.controller;

import com.team6.backend.common.response.ResponseMessage;
import com.team6.backend.member.dto.request.MemberLoginRequestDto;
import com.team6.backend.member.dto.request.MemberSignupRequestDto;
import com.team6.backend.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/member/signup")
    public ResponseEntity<ResponseMessage<Void>> signup(@RequestBody MemberSignupRequestDto requestDto) {
        memberService.signup(requestDto);
        return new ResponseEntity<>(new ResponseMessage<>("회원가입 성공", null), HttpStatus.CREATED);
    }

}
