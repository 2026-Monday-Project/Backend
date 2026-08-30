package com.likelion.monday.domain.story.repository;

import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {

    long countByStatus(StoryStatus status);

    long countByAccountId(Long accountId);

    Page<Story> findAllByStatus(StoryStatus status, Pageable pageable);

    List<Story> findTop2ByAccountIdOrderByCreatedAtDesc(Long accountId);

    List<Story> findAllByAccountIdOrderByCreatedAtDesc(Long accountId);

    List<Story> findAllByAccountIdAndStatusOrderByCreatedAtDesc(Long accountId, StoryStatus status);
}
