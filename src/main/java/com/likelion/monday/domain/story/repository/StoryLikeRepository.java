package com.likelion.monday.domain.story.repository;

import com.likelion.monday.domain.story.entity.StoryLike;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryLikeRepository extends JpaRepository<StoryLike, Long> {

    long countByStory_AccountId(Long accountId);

    long countByAccountId(Long accountId);

    List<StoryLike> findAllByStory_AccountIdOrderByCreatedAtDesc(Long accountId);

    List<StoryLike> findAllByAccountIdOrderByCreatedAtDesc(Long accountId);
}