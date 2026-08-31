package com.likelion.monday.domain.story.repository;

import com.likelion.monday.domain.story.entity.StoryView;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoryViewRepository extends JpaRepository<StoryView, Long> {

    @Modifying
    @Query(value = "INSERT IGNORE INTO story_view (story_id, guest_key, created_at, updated_at) "
            + "VALUES (:storyId, :guestKey, :now, :now)", nativeQuery = true)
    int insertIgnoreByGuest(@Param("storyId") Long storyId, @Param("guestKey") String guestKey,
                            @Param("now") LocalDateTime now);
}