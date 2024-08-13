package com.team6.backend.comment.entity;

import com.team6.backend.member.entity.Member;
import com.team6.backend.pfmc.entity.Pfmc;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "pfmc_id", nullable = false)
    private Pfmc pfmc;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment; // 부모 댓글

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>(); // 대댓글 목록

    @Column(nullable = false)
    private Integer likes = 0; // 좋아요 수 필드

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 생성자
    public Comment(String content, Member member, Pfmc pfmc) {
        this.content = content;
        this.member = member;
        this.pfmc = pfmc;
    }

    // 대댓글 생성자
    public Comment(String content, Member member, Pfmc pfmc, Comment parentComment) {
        this.content = content;
        this.member = member;
        this.pfmc = pfmc;
        this.parentComment = parentComment;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public void increaseLikes() {
        this.likes++;
    }

    public void decreaseLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }
}
