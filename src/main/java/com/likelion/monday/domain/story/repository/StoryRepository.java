package com.likelion.monday.domain.story.repository;

import com.likelion.monday.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
}
