package com.likelion.monday.domain.mygarden.service;

import com.likelion.monday.domain.mygarden.dto.MyStorySummaryResDto;
import com.likelion.monday.domain.mygarden.mapper.MyGardenMapper;
import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryStatus;
import com.likelion.monday.domain.story.repository.StoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyGardenService {

    private final StoryRepository storyRepository;
    private final MyGardenMapper myGardenMapper;

    public List<MyStorySummaryResDto> getMyStoriesPreview(Long accountId) {
        return storyRepository.findTop2ByAccountIdOrderByCreatedAtDesc(accountId).stream()
                .map(myGardenMapper::toSummaryResDto)
                .toList();
    }

    public List<MyStorySummaryResDto> getMyStories(Long accountId, StoryStatus status) {
        List<Story> stories = status == null
                ? storyRepository.findAllByAccountIdOrderByCreatedAtDesc(accountId)
                : storyRepository.findAllByAccountIdAndStatusOrderByCreatedAtDesc(accountId, status);

        return stories.stream()
                .map(myGardenMapper::toSummaryResDto)
                .toList();
    }
}