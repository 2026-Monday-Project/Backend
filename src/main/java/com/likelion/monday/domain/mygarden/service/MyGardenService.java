package com.likelion.monday.domain.mygarden.service;

import com.likelion.monday.domain.account.entity.Account;
import com.likelion.monday.domain.account.repository.AccountRepository;
import com.likelion.monday.domain.mygarden.dto.LikedStoryResDto;
import com.likelion.monday.domain.mygarden.dto.MyActivitySummaryResDto;
import com.likelion.monday.domain.mygarden.dto.MyStorySummaryResDto;
import com.likelion.monday.domain.mygarden.dto.ReceivedLikeResDto;
import com.likelion.monday.domain.mygarden.dto.SentStoryResDto;
import com.likelion.monday.domain.mygarden.mapper.MyGardenMapper;
import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryLike;
import com.likelion.monday.domain.story.entity.StoryStatus;
import com.likelion.monday.domain.story.repository.StoryLikeRepository;
import com.likelion.monday.domain.story.repository.StoryRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyGardenService {

    private final StoryRepository storyRepository;
    private final StoryLikeRepository storyLikeRepository;
    private final AccountRepository accountRepository;
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

    public List<ReceivedLikeResDto> getReceivedLikes(Long accountId) {
        List<StoryLike> likes = storyLikeRepository.findAllByStory_AccountIdOrderByCreatedAtDesc(accountId);
        Map<Long, String> nicknames = findLikerNicknames(likes);

        return likes.stream()
                .map(like -> myGardenMapper.toReceivedLikeResDto(like, findLikerNickname(like, nicknames)))
                .toList();
    }

    public List<LikedStoryResDto> getLikedStories(Long accountId) {
        return storyLikeRepository.findAllByAccountIdOrderByCreatedAtDesc(accountId).stream()
                .map(myGardenMapper::toLikedStoryResDto)
                .toList();
    }

    private Map<Long, String> findLikerNicknames(List<StoryLike> likes) {
        List<Long> accountIds = likes.stream()
                .map(StoryLike::getAccountId)
                .filter(id -> id != null)
                .distinct()
                .toList();
        if (accountIds.isEmpty()) {
            return Map.of();
        }

        return accountRepository.findAllById(accountIds).stream()
                .collect(Collectors.toMap(Account::getId, Account::getNickname));
    }

    private String findLikerNickname(StoryLike like, Map<Long, String> nicknames) {
        if (like.getAccountId() == null) {
            return null;
        }
        return nicknames.get(like.getAccountId());
    }
}