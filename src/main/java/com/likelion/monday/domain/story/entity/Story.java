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
import lombok.Builder;
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
    private StoryStatus status = StoryStatus.PENDING;

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

    @Builder
    private Story(Long accountId, String petName, String petType, Integer petAge, String title, String content,
                  boolean privacyConsent, boolean contentPolicyConsent, boolean publicConsent,
                  boolean introduceConsent, boolean snsConsent) {
        this.accountId = accountId;
        this.petName = petName;
        this.petType = petType;
        this.petAge = petAge;
        this.title = title;
        this.content = content;
        this.privacyConsent = privacyConsent;
        this.contentPolicyConsent = contentPolicyConsent;
        this.publicConsent = publicConsent;
        this.introduceConsent = introduceConsent;
        this.snsConsent = snsConsent;
    }

    /**
     * 검토중 상태의 사연 내용을 수정한다.
     * 필수 동의 항목은 제출 시점에 확정되므로 수정 대상에서 제외하고, 선택 동의만 다시 받는다.
     */
    public void update(String petName, String petType, Integer petAge, String title, String content,
                       boolean introduceConsent, boolean snsConsent) {
        this.petName = petName;
        this.petType = petType;
        this.petAge = petAge;
        this.title = title;
        this.content = content;
        this.introduceConsent = introduceConsent;
        this.snsConsent = snsConsent;
    }

    // 운영진 검수 결과를 반영한다. 공개된 뒤에도 부적절하다고 판단되면 다시 비공개로 돌릴 수 있다.
    public void updateStatus(StoryStatus status) {
        this.status = status;
    }

    // 운영진 검토가 끝나 공개/비공개가 결정된 사연은 더 이상 수정할 수 없다.
    public boolean isEditable() {
        return this.status == StoryStatus.PENDING;
    }

    public boolean isOwnedBy(Long accountId) {
        return this.accountId.equals(accountId);
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}
