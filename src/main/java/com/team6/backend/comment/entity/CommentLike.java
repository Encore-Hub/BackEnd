package com.team6.backend.comment.entity;

import com.team6.backend.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment_likes")
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private boolean liked = true; // 기본값을 true로 설정

    public CommentLike(Comment comment, Member member) {
        this.comment = comment;
        this.member = member;
        this.liked = true; // 생성 시 liked는 기본적으로 true
    }

    public void toggleLike() {
        this.liked = !this.liked; // 좋아요 상태를 토글
    }
}
