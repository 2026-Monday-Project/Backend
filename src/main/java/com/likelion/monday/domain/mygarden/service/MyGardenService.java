package com.likelion.monday.domain.mygarden.service;

import com.likelion.monday.domain.mygarden.dto.*;
import com.likelion.monday.domain.mygarden.exception.MyGardenErrorCode;
import com.likelion.monday.domain.mygarden.mapper.MyGardenMapper;
import com.likelion.monday.domain.notification.entity.Notification;
import com.likelion.monday.domain.notification.repository.NotificationRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyGardenService {

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;

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

    // 내 사연 전체/필터 조회. status가 null이면 전체로 간주한다. 보낸 사연 목록으로도 함께 사용한다.
    public PageResDto<MyStorySummaryResDto> getMyStories(Long accountId, StoryStatus status, int page, int size) {
        Pageable pageable = pageable(page, size);
        Page<Story> stories = status == null
                ? storyRepository.findAllByAccountId(accountId, pageable)
                : storyRepository.findAllByAccountIdAndStatus(accountId, status, pageable);

        return myGardenMapper.toMyStoryPageResDto(stories);
    }

    public MyActivitySummaryResDto getActivitySummary(Long accountId) {
        long sentStoryCount = storyRepository.countByAccountId(accountId);
        long receivedLikeCount = storyLikeRepository.countByStory_AccountId(accountId);
        long likedStoryCount = storyLikeRepository.countByAccountId(accountId);

        return new MyActivitySummaryResDto(sentStoryCount, receivedLikeCount, likedStoryCount);
    }

    // 받은 공감: 내가 쓴 사연들에 달린 공감
    public PageResDto<ReceivedLikeResDto> getReceivedLikes(Long accountId, int page, int size) {
        Page<com.likelion.monday.domain.story.entity.StoryLike> likes =
                storyLikeRepository.findAllByStory_AccountId(accountId, pageable(page, size));

        return myGardenMapper.toReceivedLikePageResDto(likes);
    }

    public PageResDto<LikedStoryResDto> getLikedStories(Long accountId, int page, int size) {
        Page<com.likelion.monday.domain.story.entity.StoryLike> likes =
                storyLikeRepository.findAllByAccountId(accountId, pageable(page, size));

        return myGardenMapper.toLikedStoryPageResDto(likes);
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

    public PageResDto<NotificationSummaryResDto> getNotifications(Long accountId, int page, int size) {
        Page<Notification> notifications = notificationRepository.findAllByAccountId(accountId, pageable(page, size));

        return myGardenMapper.toNotificationPageResDto(notifications);
    }

    @Transactional
    public NotificationDetailResDto getNotificationDetail(Long accountId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(MyGardenErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getAccountId().equals(accountId)) {
            throw new CustomException(MyGardenErrorCode.NOTIFICATION_ACCESS_DENIED);
        }

        notification.markAsRead();

        return myGardenMapper.toNotificationDetailResDto(notification);
    }

    // 목록 API 전반에서 쓰는 페이지 요청 생성. 최신순 정렬에 id를 보조 기준으로 더해 순서를 보장한다.
    private Pageable pageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, MIN_PAGE_SIZE), MAX_PAGE_SIZE);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id"));

        return PageRequest.of(safePage, safeSize, sort);
    }
}