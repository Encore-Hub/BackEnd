package com.team6.backend.security;

import com.team6.backend.common.exception.EncoreHubException;
import com.team6.backend.member.entity.Member;
import com.team6.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.team6.backend.common.exception.ErrorCode.NOT_FOUND_MEMBER;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EncoreHubException(NOT_FOUND_MEMBER));

        return new UserDetailsImpl(member);
    }

    public UserDetails loadUserById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EncoreHubException(NOT_FOUND_MEMBER));

        return new UserDetailsImpl(member);
    }
}

