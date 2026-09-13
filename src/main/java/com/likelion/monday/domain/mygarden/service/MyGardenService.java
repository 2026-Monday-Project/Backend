package com.likelion.monday.domain.mygarden.service;

import com.likelion.monday.domain.mygarden.constant.MyGardenSort;
import com.likelion.monday.domain.mygarden.dto.*;
import com.likelion.monday.domain.mygarden.exception.MyGardenErrorCode;
import com.likelion.monday.domain.mygarden.mapper.MyGardenMapper;
import com.likelion.monday.domain.notification.entity.Notification;
import com.likelion.monday.domain.notification.repository.NotificationRepository;
import com.likelion.monday.domain.story.dto.StoryImageResDto;
import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryImage;
import com.likelion.monday.domain.story.entity.StoryLike;
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
    public PageResDto<MyStorySummaryResDto> getMyStories(
            Long accountId, StoryStatus status, MyGardenSort sort, int page, int size) {
        Pageable pageable = pageable(page, size, sort);
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

    // 받은 공감: 내가 쓴 사연들에 달린 공감. 디자인상 정렬 옵션 없이 항상 최신순(공감 시각 기준)이다.
    public PageResDto<ReceivedLikeResDto> getReceivedLikes(Long accountId, int page, int size) {
        Page<StoryLike> likes =
                storyLikeRepository.findAllByStory_AccountId(accountId, pageable(page, size, MyGardenSort.LATEST));
        Map<Long, String> thumbnails = findLikeThumbnails(likes.getContent());

        List<ReceivedLikeResDto> content = likes.getContent().stream()
                .map(like -> myGardenMapper.toReceivedLikeResDto(like, thumbnails.get(like.getStory().getId())))
                .toList();

        return new PageResDto<>(
                content,
                likes.getNumber(),
                likes.getSize(),
                likes.getTotalElements(),
                likes.getTotalPages());
    }

    public PageResDto<LikedStoryResDto> getLikedStories(Long accountId, MyGardenSort sort, int page, int size) {
        Page<StoryLike> likes =
                storyLikeRepository.findAllByAccountId(accountId, pageableForLikes(page, size, sort));
        Map<Long, String> thumbnails = findLikeThumbnails(likes.getContent());

        List<LikedStoryResDto> content = likes.getContent().stream()
                .map(like -> myGardenMapper.toLikedStoryResDto(like, thumbnails.get(like.getStory().getId())))
                .toList();

        return new PageResDto<>(
                content,
                likes.getNumber(),
                likes.getSize(),
                likes.getTotalElements(),
                likes.getTotalPages());
    }

    // 존재하지 않는 사연과 남의 사연을 같은 응답(404)으로 처리해, 사연 존재 여부가 외부로 새어나가지 않게 한다.
    public MyStoryDetailResDto getMyStoryDetail(Long accountId, Long storyId) {
        Story story = storyRepository.findById(storyId)
                .filter(found -> found.isOwnedBy(accountId))
                .orElseThrow(() -> new CustomException(MyGardenErrorCode.STORY_NOT_FOUND));

        // 수정 화면에서 남길 사진을 지정하려면 URL뿐 아니라 사진 ID가 필요하다.
        List<StoryImageResDto> images = storyImageRepository.findByStory_IdOrderBySortOrderAsc(storyId).stream()
                .map(StoryImageResDto::from)
                .toList();

        return myGardenMapper.toDetailResDto(story, images);
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
        Page<Notification> notifications =
                notificationRepository.findAllByAccountId(accountId, pageable(page, size, MyGardenSort.LATEST));

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

    // 공감 목록(받은 공감/공감한 사연)에서 사연 대표 사진을 한 번에 묶어 가져온다.
    private Map<Long, String> findLikeThumbnails(List<StoryLike> likes) {
        List<Story> stories = likes.stream()
                .map(StoryLike::getStory)
                .distinct()
                .toList();

        return findThumbnails(stories);
    }

    // 내 사연 목록 정렬. Story 엔티티 필드에 바로 접근한다.
    private Pageable pageable(int page, int size, MyGardenSort sort) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, MIN_PAGE_SIZE), MAX_PAGE_SIZE);
        Sort resolvedSort = switch (sort) {
            case VIEWS -> Sort.by(Sort.Direction.DESC, "viewCount").and(Sort.by(Sort.Direction.DESC, "id"));
            case LIKES -> Sort.by(Sort.Direction.DESC, "likeCount").and(Sort.by(Sort.Direction.DESC, "id"));
            case LATEST -> Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id"));
        };

        return PageRequest.of(safePage, safeSize, resolvedSort);
    }

    // 공감 목록(StoryLike) 정렬. 최신순은 "공감을 남긴 시각"(StoryLike.createdAt) 기준이고,
    // 조회순/공감순은 사연 자체의 속성이라 story.필드를 거쳐 접근한다.
    private Pageable pageableForLikes(int page, int size, MyGardenSort sort) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, MIN_PAGE_SIZE), MAX_PAGE_SIZE);
        Sort resolvedSort = switch (sort) {
            case VIEWS -> Sort.by(Sort.Direction.DESC, "story.viewCount").and(Sort.by(Sort.Direction.DESC, "story.id"));
            case LIKES -> Sort.by(Sort.Direction.DESC, "story.likeCount").and(Sort.by(Sort.Direction.DESC, "story.id"));
            case LATEST -> Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id"));
        };

        return PageRequest.of(safePage, safeSize, resolvedSort);
    }
}