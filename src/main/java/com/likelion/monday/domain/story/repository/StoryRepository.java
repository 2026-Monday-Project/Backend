package com.likelion.monday.domain.story.repository;

import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {

    long countByStatus(StoryStatus status);

    Page<Story> findAllByStatus(StoryStatus status, Pageable pageable);
}
