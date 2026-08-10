package com.likelion.monday.domain.story.entity;

import com.likelion.monday.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * accountId는 연관관계가 아닌 bare Long으로 계정(account 도메인)을 참조한다.
 */
@Entity
@Table(name = "story")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Story extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(nullable = false, length = 50)
    private String petName;

    @Column(nullable = false, length = 50)
    private String petType;

    @Column(nullable = false)
    private Integer petAge;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StoryStatus status;

    @Column(nullable = false)
    private boolean performanceCandidate;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private boolean privacyConsent;

    @Column(nullable = false)
    private boolean contentPolicyConsent;

    @Column(nullable = false)
    private boolean publicConsent;

    @Column(nullable = false)
    private boolean introduceConsent;

    @Column(nullable = false)
    private boolean snsConsent;
}
