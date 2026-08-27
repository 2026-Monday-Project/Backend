package com.likelion.monday.domain.story.repository;

import com.likelion.monday.domain.story.entity.StoryView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryViewRepository extends JpaRepository<StoryView, Long> {

    boolean existsByStory_IdAndAccountId(Long storyId, Long accountId);

    boolean existsByStory_IdAndGuestKey(Long storyId, String guestKey);
}