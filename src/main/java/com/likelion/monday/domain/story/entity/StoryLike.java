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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비로그인 사용자의 중복 공감 방지를 위해 accountId(로그인 시)와 guestKey(비로그인 시) 중 하나로 식별한다.
 */
@Entity
@Table(name = "story_like")
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
}
