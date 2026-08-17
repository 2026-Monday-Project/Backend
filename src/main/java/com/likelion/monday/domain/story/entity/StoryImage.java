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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * story 도메인 내부 참조라 실제 JPA 연관관계(@ManyToOne)를 사용한다.
 */
@Entity
@Table(name = "story_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoryImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private int sortOrder;

    @Builder
    private StoryImage(Story story, String imageUrl, int sortOrder) {
        this.story = story;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    // 사연 수정 중 중간 사진이 삭제되면 남은 사진의 노출 순서를 앞에서부터 다시 매긴다.
    public void updateSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
