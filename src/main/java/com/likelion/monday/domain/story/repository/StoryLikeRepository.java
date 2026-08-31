package com.likelion.monday.domain.story.repository;

import com.likelion.monday.domain.story.entity.StoryLike;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoryLikeRepository extends JpaRepository<StoryLike, Long> {

    long countByStory_AccountId(Long accountId);

    long countByAccountId(Long accountId);

    List<StoryLike> findAllByStory_AccountIdOrderByCreatedAtDesc(Long accountId);

    List<StoryLike> findAllByAccountIdOrderByCreatedAtDesc(Long accountId);

    long deleteByStory_IdAndAccountId(Long storyId, Long accountId);

    long deleteByStory_IdAndGuestKey(Long storyId, String guestKey);

    /**
     * 유니크 제약(story_id, account_id)에 걸리면 삽입을 조용히 무시한다.
     * exists 확인 후 insert하던 기존 방식과 달리 하나의 원자적 쿼리라 동시 요청에도 안전하다.
     * BaseEntity의 @CreatedDate/@LastModifiedDate는 JPA 저장 경로에서만 채워지므로 직접 값을 넣어준다.
     * @return 실제로 삽입된 행 수 (0 또는 1)
     */
    @Modifying
    @Query(value = "INSERT IGNORE INTO story_like (story_id, account_id, created_at, updated_at) "
            + "VALUES (:storyId, :accountId, :now, :now)", nativeQuery = true)
    int insertIgnoreByAccount(@Param("storyId") Long storyId, @Param("accountId") Long accountId,
                              @Param("now") LocalDateTime now);

    @Modifying
    @Query(value = "INSERT IGNORE INTO story_like (story_id, guest_key, created_at, updated_at) "
            + "VALUES (:storyId, :guestKey, :now, :now)", nativeQuery = true)
    int insertIgnoreByGuest(@Param("storyId") Long storyId, @Param("guestKey") String guestKey,
                            @Param("now") LocalDateTime now);
}