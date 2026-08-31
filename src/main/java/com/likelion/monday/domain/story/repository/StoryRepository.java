package com.likelion.monday.domain.story.repository;

import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoryRepository extends JpaRepository<Story, Long> {

    long countByStatus(StoryStatus status);

    long countByAccountId(Long accountId);

    Page<Story> findAllByStatus(StoryStatus status, Pageable pageable);

    List<Story> findTop2ByAccountIdOrderByCreatedAtDesc(Long accountId);

    List<Story> findAllByAccountIdOrderByCreatedAtDesc(Long accountId);

    List<Story> findAllByAccountIdAndStatusOrderByCreatedAtDesc(Long accountId, StoryStatus status);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Story s SET s.viewCount = s.viewCount + 1 WHERE s.id = :storyId")
    void increaseViewCount(@Param("storyId") Long storyId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Story s SET s.likeCount = s.likeCount + 1 WHERE s.id = :storyId")
    void increaseLikeCount(@Param("storyId") Long storyId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Story s SET s.likeCount = s.likeCount - 1 WHERE s.id = :storyId AND s.likeCount > 0")
    void decreaseLikeCount(@Param("storyId") Long storyId);
}
