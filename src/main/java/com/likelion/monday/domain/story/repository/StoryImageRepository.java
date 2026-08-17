package com.likelion.monday.domain.story.repository;

import com.likelion.monday.domain.story.entity.StoryImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryImageRepository extends JpaRepository<StoryImage, Long> {

    List<StoryImage> findByStory_IdOrderBySortOrderAsc(Long storyId);
}
