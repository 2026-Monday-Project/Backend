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

    Page<Story> findAllByAccountId(Long accountId, Pageable pageable);

    Page<Story> findAllByAccountIdAndStatus(Long accountId, StoryStatus status, Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Story s SET s.viewCount = s.viewCount + 1 WHERE s.id = :storyId")
    void increaseViewCount(@Param("storyId") Long storyId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Story s SET s.likeCount = s.likeCount + 1 WHERE s.id = :storyId")
    void increaseLikeCount(@Param("storyId") Long storyId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Story s SET s.likeCount = s.likeCount - 1 WHERE s.id = :storyId AND s.likeCount > 0")
    void decreaseLikeCount(@Param("storyId") Long storyId);

    /**
     * 특정 계정이 쓴 사연 중 공감을 1건 이상 받은 사연을, 가장 최근에 공감받은 시각 순으로 조회한다.
     * "받은 공감" 목록은 사연 단위로 묶어서 보여줘야 하므로 story_like를 GROUP BY story_id로 묶는다.
     * 검토 결과 비공개로 전환된 사연은 제외한다.
     */
    @Query(value = "SELECT s.* FROM story s "
            + "JOIN story_like sl ON sl.story_id = s.id "
            + "WHERE s.account_id = :accountId AND s.status = :status "
            + "GROUP BY s.id "
            + "ORDER BY MAX(sl.created_at) DESC, s.id DESC",
            countQuery = "SELECT COUNT(DISTINCT s.id) FROM story s "
                    + "JOIN story_like sl ON sl.story_id = s.id "
                    + "WHERE s.account_id = :accountId AND s.status = :status",
            nativeQuery = true)
    Page<Story> findStoriesReceivingLikes(@Param("accountId") Long accountId,
                                          @Param("status") String status, Pageable pageable);
}