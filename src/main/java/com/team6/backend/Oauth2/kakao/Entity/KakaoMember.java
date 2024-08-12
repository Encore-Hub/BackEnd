package com.team6.backend.Oauth2.kakao.Entity;

import com.team6.backend.member.entity.MemberRoleEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class KakaoMember {
    @Id
    @GeneratedValue
    private  Long memberId;
    private String username;
    private String email;
    @Enumerated(EnumType.STRING)
    private MemberRoleEnum role;

}
