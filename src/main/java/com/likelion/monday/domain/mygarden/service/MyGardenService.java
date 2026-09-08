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
import com.likelion.monday.domain.story.repository.StoryViewRepository;
import com.likelion.monday.global.exception.CustomException;
import com.likelion.monday.global.storage.ImageStorage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    private final StoryViewRepository storyViewRepository;
    private final MyGardenMapper myGardenMapper;
    private final ImageStorage imageStorage;
    private final NotificationRepository notificationRepository;

    public List<MyStorySummaryResDto> getMyStoriesPreview(Long accountId) {
        List<Story> stories = storyRepository.findTop2ByAccountIdOrderByCreatedAtDesc(accountId);
        Map<Long, String> thumbnails = findThumbnails(stories);

        return stories.stream()
                .map(story -> myGardenMapper.toSummaryResDto(story, thumbnails.get(story.getId())))
                .toList();
    }

    // 내 사연 전체/필터 조회. status가 null이면 전체로 간주한다. 보낸 사연 목록으로도 함께 사용한다.
    public PageResDto<MyStorySummaryResDto> getMyStories(Long accountId, StoryStatus status, int page, int size) {
        Pageable pageable = pageable(page, size);
        Page<Story> stories = status == null
                ? storyRepository.findAllByAccountId(accountId, pageable)
                : storyRepository.findAllByAccountIdAndStatus(accountId, status, pageable);
        Map<Long, String> thumbnails = findThumbnails(stories.getContent());

        List<MyStorySummaryResDto> content = stories.getContent().stream()
                .map(story -> myGardenMapper.toSummaryResDto(story, thumbnails.get(story.getId())))
                .toList();

        return new PageResDto<>(
                content,
                stories.getNumber(),
                stories.getSize(),
                stories.getTotalElements(),
                stories.getTotalPages());
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

    // 존재하지 않는 사연과 남의 사연을 같은 응답(404)으로 처리해, 사연 존재 여부가 외부로 새어나가지 않게 한다.
    public MyStoryDetailResDto getMyStoryDetail(Long accountId, Long storyId) {
        Story story = storyRepository.findById(storyId)
                .filter(found -> found.isOwnedBy(accountId))
                .orElseThrow(() -> new CustomException(MyGardenErrorCode.STORY_NOT_FOUND));

        List<String> imageUrls = storyImageRepository.findByStory_IdOrderBySortOrderAsc(storyId).stream()
                .map(StoryImage::getImageUrl)
                .toList();

        return myGardenMapper.toDetailResDto(story, imageUrls);
    }

    @Transactional
    public void deleteMyStory(Long accountId, Long storyId) {
        Story story = storyRepository.findById(storyId)
                .filter(found -> found.isOwnedBy(accountId))
                .orElseThrow(() -> new CustomException(MyGardenErrorCode.STORY_NOT_FOUND));

        List<StoryImage> images = storyImageRepository.findByStory_IdOrderBySortOrderAsc(storyId);
        storyImageRepository.deleteAll(images);
        storyImageRepository.flush();
        images.forEach(image -> imageStorage.delete(image.getImageUrl()));

        // 사연을 참조하는 공감/조회 기록을 먼저 정리해야 외래키 제약 없이 삭제된다.
        storyLikeRepository.deleteByStory_Id(storyId);
        storyViewRepository.deleteByStory_Id(storyId);

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

    // 존재하지 않는 알림과 남의 알림을 같은 응답(404)으로 처리해, 알림 존재 여부가 외부로 새어나가지 않게 한다.
    @Transactional
    public NotificationDetailResDto getNotificationDetail(Long accountId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .filter(found -> found.getAccountId().equals(accountId))
                .orElseThrow(() -> new CustomException(MyGardenErrorCode.NOTIFICATION_NOT_FOUND));

        notification.markAsRead();

        return myGardenMapper.toNotificationDetailResDto(notification);
    }

    // 목록에서 사연마다 대표 사진을 따로 조회하면 N+1이 되므로, 한 번에 가져와 묶는다.
    private Map<Long, String> findThumbnails(List<Story> stories) {
        List<Long> storyIds = stories.stream()
                .map(Story::getId)
                .toList();
        if (storyIds.isEmpty()) {
            return Map.of();
        }

        return storyImageRepository.findByStory_IdInOrderBySortOrderAsc(storyIds).stream()
                .collect(Collectors.toMap(
                        image -> image.getStory().getId(),
                        StoryImage::getImageUrl,
                        (first, second) -> first));
    }

    // 목록 API 전반에서 쓰는 페이지 요청 생성. 최신순 정렬에 id를 보조 기준으로 더해 순서를 보장한다.
    private Pageable pageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, MIN_PAGE_SIZE), MAX_PAGE_SIZE);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id"));

        return PageRequest.of(safePage, safeSize, sort);
    }
}