package com.likelion.monday.domain.story.repository;

import com.likelion.monday.domain.story.entity.StoryImage;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryImageRepository extends JpaRepository<StoryImage, Long> {

    List<StoryImage> findByStory_IdOrderBySortOrderAsc(Long storyId);

    // 목록 화면에서 사연마다 조회하면 N+1이 되므로, 한 페이지 분량을 한 번에 가져온다.
    List<StoryImage> findByStory_IdInOrderBySortOrderAsc(Collection<Long> storyIds);
}
