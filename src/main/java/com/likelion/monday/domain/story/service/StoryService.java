package com.likelion.monday.domain.story.service;

import com.likelion.monday.domain.account.entity.Account;
import com.likelion.monday.domain.account.exception.AccountErrorCode;
import com.likelion.monday.domain.account.repository.AccountRepository;
import com.likelion.monday.domain.story.constant.StorySort;
import com.likelion.monday.domain.story.dto.StoryCardResDto;
import com.likelion.monday.domain.story.dto.StoryCreateReqDto;
import com.likelion.monday.domain.story.dto.StoryDetailResDto;
import com.likelion.monday.domain.story.dto.StoryPageResDto;
import com.likelion.monday.domain.story.dto.StoryUpdateReqDto;
import com.likelion.monday.domain.story.dto.StoryWriteResDto;
import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryImage;
import com.likelion.monday.domain.story.entity.StoryStatus;
import com.likelion.monday.domain.story.entity.StoryView;
import com.likelion.monday.domain.story.exception.StoryErrorCode;
import com.likelion.monday.domain.story.mapper.StoryMapper;
import com.likelion.monday.domain.story.repository.StoryImageRepository;
import com.likelion.monday.domain.story.repository.StoryRepository;
import com.likelion.monday.domain.story.repository.StoryViewRepository;
import com.likelion.monday.global.exception.CustomException;
import com.likelion.monday.global.storage.ImageStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class StoryService {

    private static final int MAX_IMAGE_COUNT = 5;
    private static final String IMAGE_DIRECTORY = "story";

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 100;

    private final StoryRepository storyRepository;
    private final StoryImageRepository storyImageRepository;
    private final StoryViewRepository storyViewRepository;
    private final AccountRepository accountRepository;
    private final StoryMapper storyMapper;
    private final ImageStorage imageStorage;

    /**
     * 공개(PUBLIC)된 사연만 카드 목록으로 보여준다.
     * 목록에서 사연마다 대표 사진·작성자를 조회하면 N+1이 되므로 한 페이지 분량을 한 번에 가져와 묶는다.
     */
    @Transactional(readOnly = true)
    public StoryPageResDto getStories(StorySort sort, int page, int size) {
        Pageable pageable = PageRequest.of(adjustPage(page), adjustSize(size), sort.toSort());
        Page<Story> stories = storyRepository.findAllByStatus(StoryStatus.PUBLIC, pageable);

        Map<Long, String> thumbnails = findThumbnails(stories.getContent());
        Map<Long, String> nicknames = findNicknames(stories.getContent());

        List<StoryCardResDto> cards = stories.getContent().stream()
                .map(story -> storyMapper.toCardResDto(
                        story,
                        nicknames.get(story.getAccountId()),
                        thumbnails.get(story.getId())))
                .toList();

        return new StoryPageResDto(
                cards,
                stories.getNumber(),
                stories.getSize(),
                stories.getTotalElements(),
                stories.getTotalPages());
    }

    /**
     * 공개(PUBLIC)된 사연 하나를 상세로 보여준다.
     * 같은 게스트가 같은 사연을 다시 봐도 조회수가 중복으로 올라가지 않도록, 조회 기록이 없을 때만 기록하고 조회수를 올린다.
     */
    public StoryDetailResDto getStory(Long storyId, String guestKey) {
        Story story = storyRepository.findById(storyId)
                .filter(s -> s.getStatus() == StoryStatus.PUBLIC)
                .orElseThrow(() -> new CustomException(StoryErrorCode.STORY_NOT_FOUND));

        boolean viewed = recordView(story, guestKey);
        int viewCount = viewed ? story.getViewCount() + 1 : story.getViewCount();

        Account account = accountRepository.findById(story.getAccountId())
                .orElseThrow(() -> new CustomException(AccountErrorCode.ACCOUNT_NOT_FOUND));
        List<String> imageUrls = storyImageRepository.findByStory_IdOrderBySortOrderAsc(storyId).stream()
                .map(StoryImage::getImageUrl)
                .toList();

        return storyMapper.toDetailResDto(story, viewCount, account.getNickname(), imageUrls);
    }

    /**
     * 사연을 등록한다.
     * 이메일은 계정 식별자이므로 처음 보는 이메일이면 계정을 만들고, 이미 있으면 그 계정에 사연을 하나 더 추가한다.
     * 제출된 사연은 운영진 검토 전이므로 PENDING(검토중) 상태로 저장된다.
     */
    public StoryWriteResDto createStory(StoryCreateReqDto request, List<MultipartFile> images) {
        List<MultipartFile> newImages = filterEmptyFiles(images);
        validateImages(newImages, 0);

        Account account = findOrCreateAccount(request.email(), request.nickname());
        Story story = storyRepository.save(storyMapper.toEntity(request, account.getId()));
        List<String> imageUrls = uploadImages(story, newImages, 0);

        return storyMapper.toWriteResDto(story, account.getNickname(), imageUrls);
    }

    /**
     * 검토중인 본인 사연을 수정한다.
     * 작성자 확인은 로그인 토큰에서 꺼낸 계정으로 처리한다.
     * 남길 사진은 keepImageIds로 받고, 새로 올린 사진을 그 뒤에 이어 붙여 노출 순서를 다시 매긴다.
     */
    public StoryWriteResDto updateStory(Long accountId, Long storyId, StoryUpdateReqDto request,
                                        List<MultipartFile> images) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(StoryErrorCode.STORY_NOT_FOUND));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomException(AccountErrorCode.ACCOUNT_NOT_FOUND));

        if (!story.isOwnedBy(account.getId())) {
            throw new CustomException(StoryErrorCode.STORY_ACCESS_DENIED);
        }
        if (!story.isEditable()) {
            throw new CustomException(StoryErrorCode.STORY_NOT_EDITABLE);
        }

        List<Long> keepImageIds = request.keepImageIds() == null ? List.of() : request.keepImageIds();
        List<StoryImage> savedImages = storyImageRepository.findByStory_IdOrderBySortOrderAsc(storyId);
        List<StoryImage> keptImages = savedImages.stream()
                .filter(image -> keepImageIds.contains(image.getId()))
                .toList();
        if (keptImages.size() != keepImageIds.size()) {
            throw new CustomException(StoryErrorCode.STORY_IMAGE_NOT_FOUND);
        }

        List<MultipartFile> newImages = filterEmptyFiles(images);
        validateImages(newImages, keptImages.size());

        story.update(request.petName(), request.petType(), request.petAge(), request.title(), request.content(),
                request.introduceConsent(), request.snsConsent());

        List<StoryImage> removedImages = savedImages.stream()
                .filter(image -> !keepImageIds.contains(image.getId()))
                .toList();
        deleteImages(removedImages);

        List<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < keptImages.size(); i++) {
            StoryImage keptImage = keptImages.get(i);
            keptImage.updateSortOrder(i);
            imageUrls.add(keptImage.getImageUrl());
        }
        imageUrls.addAll(uploadImages(story, newImages, keptImages.size()));

        return storyMapper.toWriteResDto(story, account.getNickname(), imageUrls);
    }

    private Account findOrCreateAccount(String email, String nickname) {
        return accountRepository.findByEmail(email)
                .map(account -> updateNicknameIfChanged(account, nickname))
                .orElseGet(() -> createAccount(email, nickname));
    }

    // 같은 이메일로 다시 사연을 보낼 때 닉네임을 바꿨다면 계정 닉네임도 함께 갱신한다.
    private Account updateNicknameIfChanged(Account account, String nickname) {
        if (!account.getNickname().equals(nickname)) {
            validateNicknameNotDuplicated(nickname);
            account.updateNickname(nickname);
        }

        return account;
    }

    private Account createAccount(String email, String nickname) {
        validateNicknameNotDuplicated(nickname);

        return accountRepository.save(Account.builder()
                .email(email)
                .nickname(nickname)
                .build());
    }

    private void validateNicknameNotDuplicated(String nickname) {
        if (accountRepository.existsByNickname(nickname)) {
            throw new CustomException(StoryErrorCode.NICKNAME_DUPLICATED);
        }
    }

    // 프론트에서 빈 파트를 함께 보내는 경우가 있어 실제 내용이 있는 파일만 남긴다.
    private List<MultipartFile> filterEmptyFiles(List<MultipartFile> images) {
        if (images == null) {
            return List.of();
        }

        return images.stream()
                .filter(image -> image != null && !image.isEmpty())
                .toList();
    }

    /**
     * 파일을 하나라도 저장하기 전에 개수와 형식을 먼저 검증한다.
     * 중간에 실패해서 일부만 올라간 상태로 남는 것을 막기 위함이다.
     */
    private void validateImages(List<MultipartFile> images, int keptImageCount) {
        if (keptImageCount + images.size() > MAX_IMAGE_COUNT) {
            throw new CustomException(StoryErrorCode.IMAGE_COUNT_EXCEEDED);
        }

        for (MultipartFile image : images) {
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new CustomException(StoryErrorCode.INVALID_IMAGE_TYPE);
            }
        }
    }

    private List<String> uploadImages(Story story, List<MultipartFile> images, int startSortOrder) {
        List<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            String imageUrl = imageStorage.upload(images.get(i), IMAGE_DIRECTORY);
            storyImageRepository.save(StoryImage.builder()
                    .story(story)
                    .imageUrl(imageUrl)
                    .sortOrder(startSortOrder + i)
                    .build());
            imageUrls.add(imageUrl);
        }

        return imageUrls;
    }

    private void deleteImages(List<StoryImage> images) {
        if (images.isEmpty()) {
            return;
        }

        storyImageRepository.deleteAll(images);
        storyImageRepository.flush();
        images.forEach(image -> imageStorage.delete(image.getImageUrl()));
    }

    /**
     * 조회 기록이 없을 때만 저장하고 조회수를 올린다.
     * exists 확인과 insert 사이에 동시 요청이 들어올 수 있으므로, 유니크 제약 위반도 함께 잡아서 무시한다.
     */
    private boolean recordView(Story story, String guestKey) {
        if (storyViewRepository.existsByStory_IdAndGuestKey(story.getId(), guestKey)) {
            return false;
        }

        try {
            storyViewRepository.save(StoryView.builder()
                    .story(story)
                    .guestKey(guestKey)
                    .build());
            storyRepository.increaseViewCount(story.getId());
            return true;
        } catch (DataIntegrityViolationException e) {
            // 동시 요청으로 이미 기록된 경우, 조회수는 올리지 않고 그대로 둔다.
            return false;
        }
    }

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

    private int adjustPage(int page) {
        return Math.max(page, 0);
    }
}
