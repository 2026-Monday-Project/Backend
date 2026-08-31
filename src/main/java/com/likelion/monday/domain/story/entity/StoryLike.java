package com.likelion.monday.domain.story.entity;

import com.likelion.monday.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비로그인 사용자의 중복 공감 방지를 위해 accountId(로그인 시)와 guestKey(비로그인 시) 중 하나로 식별한다.
 * 같은 사연에 같은 계정/게스트가 중복으로 공감하지 못하도록 유니크 제약을 건다.
 * accountId/guestKey 중 정확히 하나만 채워지도록 하는 검증은 공감하기 API를 만들 때 서비스 레이어에서 처리한다.
 */
@Entity
@Table(
        name = "story_like",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_story_like_account", columnNames = {"story_id", "account_id"}),
                @UniqueConstraint(name = "uk_story_like_guest", columnNames = {"story_id", "guest_key"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoryLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "guest_key", length = 100)
    private String guestKey;

    @Builder
    private StoryLike(Story story, Long accountId, String guestKey) {
        this.story = story;
        this.accountId = accountId;
        this.guestKey = guestKey;
    }
}
