package com.likelion.monday.domain.mygarden.service;

import com.likelion.monday.domain.mygarden.dto.*;
import com.likelion.monday.domain.mygarden.exception.MyGardenErrorCode;
import com.likelion.monday.domain.mygarden.mapper.MyGardenMapper;
import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryImage;
import com.likelion.monday.domain.story.entity.StoryStatus;
import com.likelion.monday.domain.story.repository.StoryImageRepository;
import com.likelion.monday.domain.story.repository.StoryLikeRepository;
import com.likelion.monday.domain.story.repository.StoryRepository;
import com.likelion.monday.global.exception.CustomException;
import com.likelion.monday.global.storage.ImageStorage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.likelion.monday.domain.mygarden.dto.NotificationSummaryResDto;
import com.likelion.monday.domain.notification.repository.NotificationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyGardenService {

    private final StoryRepository storyRepository;
    private final StoryLikeRepository storyLikeRepository;
    private final StoryImageRepository storyImageRepository;
    private final MyGardenMapper myGardenMapper;
    private final ImageStorage imageStorage;
    private final NotificationRepository notificationRepository;

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

    public MyActivitySummaryResDto getActivitySummary(Long accountId) {
        long sentStoryCount = storyRepository.countByAccountId(accountId);
        long receivedLikeCount = storyLikeRepository.countByStory_AccountId(accountId);
        long likedStoryCount = storyLikeRepository.countByAccountId(accountId);

        return new MyActivitySummaryResDto(sentStoryCount, receivedLikeCount, likedStoryCount);
    }

    public List<SentStoryResDto> getSentStories(Long accountId) {
        return storyRepository.findAllByAccountIdOrderByCreatedAtDesc(accountId).stream()
                .map(myGardenMapper::toSentStoryResDto)
                .toList();
    }

    // 받은 공감: 내가 쓴 사연들에 달린 공감
    public List<ReceivedLikeResDto> getReceivedLikes(Long accountId) {
        return storyLikeRepository.findAllByStory_AccountIdOrderByCreatedAtDesc(accountId).stream()
                .map(myGardenMapper::toReceivedLikeResDto)
                .toList();
    }

    public List<LikedStoryResDto> getLikedStories(Long accountId) {
        return storyLikeRepository.findAllByAccountIdOrderByCreatedAtDesc(accountId).stream()
                .map(myGardenMapper::toLikedStoryResDto)
                .toList();
    }

    public MyStoryDetailResDto getMyStoryDetail(Long accountId, Long storyId) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(MyGardenErrorCode.STORY_NOT_FOUND));

        if (!story.isOwnedBy(accountId)) {
            throw new CustomException(MyGardenErrorCode.STORY_ACCESS_DENIED);
        }

        List<String> imageUrls = storyImageRepository.findByStory_IdOrderBySortOrderAsc(storyId).stream()
                .map(StoryImage::getImageUrl)
                .toList();

        return myGardenMapper.toDetailResDto(story, imageUrls);
    }

    @Transactional
    public void deleteMyStory(Long accountId, Long storyId) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(MyGardenErrorCode.STORY_NOT_FOUND));

        if (!story.isOwnedBy(accountId)) {
            throw new CustomException(MyGardenErrorCode.STORY_ACCESS_DENIED);
        }

        List<StoryImage> images = storyImageRepository.findByStory_IdOrderBySortOrderAsc(storyId);
        storyImageRepository.deleteAll(images);
        storyImageRepository.flush();
        images.forEach(image -> imageStorage.delete(image.getImageUrl()));

        storyRepository.delete(story);
    }

    public List<NotificationSummaryResDto> getNotificationsPreview(Long accountId) {
        return notificationRepository.findTop2ByAccountIdOrderByCreatedAtDescIdDesc(accountId).stream()
                .map(myGardenMapper::toNotificationSummaryResDto)
                .toList();
    }

    public List<NotificationSummaryResDto> getNotifications(Long accountId) {
        return notificationRepository.findAllByAccountIdOrderByCreatedAtDescIdDesc(accountId).stream()
                .map(myGardenMapper::toNotificationSummaryResDto)
                .toList();
    }
}