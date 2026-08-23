package com.likelion.monday.domain.admin.service;

import com.likelion.monday.domain.account.entity.Account;
import com.likelion.monday.domain.account.exception.AccountErrorCode;
import com.likelion.monday.domain.account.repository.AccountRepository;
import com.likelion.monday.domain.admin.constant.NotificationTemplate;
import com.likelion.monday.domain.admin.constant.StoryStatusFilter;
import com.likelion.monday.domain.admin.dto.AdminStoryCountResDto;
import com.likelion.monday.domain.admin.dto.AdminStoryDetailResDto;
import com.likelion.monday.domain.admin.dto.AdminStoryPageResDto;
import com.likelion.monday.domain.admin.dto.AdminStorySummaryResDto;
import com.likelion.monday.domain.admin.dto.NotificationDraftResDto;
import com.likelion.monday.domain.admin.dto.NotificationSendReqDto;
import com.likelion.monday.domain.admin.dto.NotificationSendResDto;
import com.likelion.monday.domain.admin.dto.StoryReviewReqDto;
import com.likelion.monday.domain.admin.exception.AdminErrorCode;
import com.likelion.monday.domain.admin.mapper.AdminStoryMapper;
import com.likelion.monday.domain.notification.entity.Notification;
import com.likelion.monday.domain.notification.repository.NotificationRepository;
import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryImage;
import com.likelion.monday.domain.story.entity.StoryStatus;
import com.likelion.monday.domain.story.exception.StoryErrorCode;
import com.likelion.monday.domain.story.repository.StoryImageRepository;
import com.likelion.monday.domain.story.repository.StoryRepository;
import com.likelion.monday.global.exception.CustomException;
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
public class AdminStoryService {

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;

    private final StoryRepository storyRepository;
    private final StoryImageRepository storyImageRepository;
    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;
    private final AdminStoryMapper adminStoryMapper;

    // 관리자 메인의 현황 카드에 쓰인다. 데이터가 없으면 0으로 내려간다.
    public AdminStoryCountResDto getStoryCount() {
        long pendingCount = storyRepository.countByStatus(StoryStatus.PENDING);
        long publicCount = storyRepository.countByStatus(StoryStatus.PUBLIC);
        long privateCount = storyRepository.countByStatus(StoryStatus.PRIVATE);

        return new AdminStoryCountResDto(
                pendingCount,
                publicCount,
                privateCount,
                pendingCount + publicCount + privateCount);
    }

    /**
     * 검수 대기가 오래된 사연부터 처리하도록 제출 순으로 정렬한다.
     * 목록에서 사연마다 사진과 작성자를 조회하면 N+1이 되므로 한 페이지 분량을 한 번에 가져와 묶는다.
     */
    public AdminStoryPageResDto getStories(StoryStatusFilter filter, int page, int size) {
        Pageable pageable = PageRequest.of(page, adjustSize(size), Sort.by(Sort.Direction.ASC, "createdAt"));
        StoryStatus status = filter.toStoryStatus();
        Page<Story> stories = status == null
                ? storyRepository.findAll(pageable)
                : storyRepository.findAllByStatus(status, pageable);

        Map<Long, String> thumbnails = findThumbnails(stories.getContent());
        Map<Long, String> nicknames = findNicknames(stories.getContent());

        List<AdminStorySummaryResDto> summaries = stories.getContent().stream()
                .map(story -> adminStoryMapper.toSummaryResDto(
                        story,
                        nicknames.get(story.getAccountId()),
                        thumbnails.get(story.getId())))
                .toList();

        return new AdminStoryPageResDto(
                summaries,
                stories.getNumber(),
                stories.getSize(),
                stories.getTotalElements(),
                stories.getTotalPages());
    }

    public AdminStoryDetailResDto getStoryDetail(Long storyId) {
        Story story = findStory(storyId);

        return toDetail(story);
    }

    /**
     * 검수 결과를 사연에 반영한다.
     * 공개된 뒤에도 부적절하다고 판단되면 비공개로 되돌릴 수 있어 PENDING만 아니면 허용한다.
     */
    @Transactional
    public AdminStoryDetailResDto reviewStory(Long storyId, StoryReviewReqDto request) {
        if (request.status() == StoryStatus.PENDING) {
            throw new CustomException(AdminErrorCode.INVALID_REVIEW_STATUS);
        }

        Story story = findStory(storyId);
        if (story.getStatus() == request.status()) {
            throw new CustomException(AdminErrorCode.STORY_ALREADY_IN_STATUS);
        }
        story.updateStatus(request.status());

        return toDetail(story);
    }

    // 관리자가 그대로 보내거나 고쳐서 보낼 수 있도록 초안만 만들어 준다. 이 시점에는 저장하지 않는다.
    public NotificationDraftResDto getNotificationDraft(Long storyId) {
        Story story = findReviewedStory(storyId);
        Account account = findAccount(story.getAccountId());
        NotificationTemplate template = NotificationTemplate.from(story.getStatus());

        return new NotificationDraftResDto(
                template.getTitle(),
                template.formatContent(account.getNickname(), story.getTitle()));
    }

    @Transactional
    public NotificationSendResDto sendNotification(Long storyId, NotificationSendReqDto request) {
        Story story = findReviewedStory(storyId);
        Account account = findAccount(story.getAccountId());

        Notification notification = notificationRepository.save(Notification.builder()
                .accountId(account.getId())
                .title(request.title())
                .content(request.content())
                .build());

        return new NotificationSendResDto(
                notification.getId(),
                story.getId(),
                account.getNickname(),
                notification.getCreatedAt());
    }

    private AdminStoryDetailResDto toDetail(Story story) {
        Account account = findAccount(story.getAccountId());
        List<String> imageUrls = storyImageRepository.findByStory_IdOrderBySortOrderAsc(story.getId()).stream()
                .map(StoryImage::getImageUrl)
                .toList();

        return adminStoryMapper.toDetailResDto(story, account, imageUrls);
    }

    private Story findStory(Long storyId) {
        return storyRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(StoryErrorCode.STORY_NOT_FOUND));
    }

    // 알림은 검수 결과를 안내하는 것이므로 아직 검토중인 사연에는 보낼 수 없다.
    private Story findReviewedStory(Long storyId) {
        Story story = findStory(storyId);
        if (story.getStatus() == StoryStatus.PENDING) {
            throw new CustomException(AdminErrorCode.NOTIFICATION_TARGET_NOT_REVIEWED);
        }

        return story;
    }

    private Account findAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomException(AccountErrorCode.ACCOUNT_NOT_FOUND));
    }

    private Map<Long, String> findThumbnails(List<Story> stories) {
        List<Long> storyIds = stories.stream()
                .map(Story::getId)
                .toList();
        if (storyIds.isEmpty()) {
            return Map.of();
        }

        // 정렬 순서가 앞선 사진이 대표 사진이므로 사연당 첫 번째만 남긴다.
        return storyImageRepository.findByStory_IdInOrderBySortOrderAsc(storyIds).stream()
                .collect(Collectors.toMap(
                        image -> image.getStory().getId(),
                        StoryImage::getImageUrl,
                        (first, second) -> first));
    }

    private Map<Long, String> findNicknames(List<Story> stories) {
        List<Long> accountIds = stories.stream()
                .map(Story::getAccountId)
                .distinct()
                .toList();
        if (accountIds.isEmpty()) {
            return Map.of();
        }

        return accountRepository.findAllById(accountIds).stream()
                .collect(Collectors.toMap(Account::getId, Account::getNickname));
    }

    private int adjustSize(int size) {
        return Math.min(Math.max(size, MIN_PAGE_SIZE), MAX_PAGE_SIZE);
    }
}
