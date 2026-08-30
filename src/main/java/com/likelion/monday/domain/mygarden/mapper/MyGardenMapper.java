package com.likelion.monday.domain.mygarden.mapper;

import com.likelion.monday.domain.mygarden.dto.LikedStoryResDto;
import com.likelion.monday.domain.mygarden.dto.MyStorySummaryResDto;
import com.likelion.monday.domain.mygarden.dto.ReceivedLikeResDto;
import com.likelion.monday.domain.mygarden.dto.SentStoryResDto;
import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryLike;
import org.springframework.stereotype.Component;

@Component
public class MyGardenMapper {

    public MyStorySummaryResDto toSummaryResDto(Story story) {
        return new MyStorySummaryResDto(
                story.getId(),
                story.getTitle(),
                story.getPetName(),
                story.getStatus(),
                story.getLikeCount(),
                story.getCreatedAt());
    }

    public SentStoryResDto toSentStoryResDto(Story story) {
        return new SentStoryResDto(
                story.getId(),
                story.getTitle(),
                story.getStatus(),
                story.getCreatedAt());
    }

    public ReceivedLikeResDto toReceivedLikeResDto(StoryLike like) {
        return new ReceivedLikeResDto(
                like.getId(),
                like.getStory().getId(),
                like.getStory().getTitle());
    }

    public LikedStoryResDto toLikedStoryResDto(StoryLike like) {
        return new LikedStoryResDto(
                like.getStory().getId(),
                like.getStory().getTitle(),
                like.getCreatedAt());
    }
}