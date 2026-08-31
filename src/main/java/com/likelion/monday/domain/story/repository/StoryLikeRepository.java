package com.likelion.monday.domain.story.repository;

import com.likelion.monday.domain.story.entity.StoryLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryLikeRepository extends JpaRepository<StoryLike, Long> {

    // 내 정원 홈 - 활동 요약: 내가 쓴 사연들이 받은 공감 수
    long countByStory_AccountId(Long accountId);

    // 내 정원 홈 - 활동 요약: 내가 남긴 공감 수
    long countByAccountId(Long accountId);

    // 받은 공감 목록 (페이지네이션)
    Page<StoryLike> findAllByStory_AccountId(Long accountId, Pageable pageable);

    // 공감한 사연 목록 (페이지네이션)
    Page<StoryLike> findAllByAccountId(Long accountId, Pageable pageable);

    boolean existsByStory_IdAndAccountId(Long storyId, Long accountId);

    boolean existsByStory_IdAndGuestKey(Long storyId, String guestKey);

    long deleteByStory_IdAndAccountId(Long storyId, Long accountId);

    long deleteByStory_IdAndGuestKey(Long storyId, String guestKey);
}