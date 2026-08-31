package com.likelion.monday.domain.mygarden.mapper;

import com.likelion.monday.domain.mygarden.dto.*;
import com.likelion.monday.domain.notification.entity.Notification;
import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryLike;
import java.util.List;
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

    public MyStoryDetailResDto toDetailResDto(Story story, List<String> imageUrls) {
        return new MyStoryDetailResDto(
                story.getId(),
                story.getTitle(),
                story.getContent(),
                story.getPetName(),
                story.getPetType(),
                story.getPetAge(),
                story.getStatus(),
                story.getViewCount(),
                story.getLikeCount(),
                story.isIntroduceConsent(),
                story.isSnsConsent(),
                imageUrls,
                story.getCreatedAt(),
                story.getUpdatedAt());
    }

    public NotificationSummaryResDto toNotificationSummaryResDto(Notification notification) {
        return new NotificationSummaryResDto(
                notification.getId(),
                notification.getTitle(),
                notification.isRead(),
                notification.getCreatedAt());
    }

    public NotificationDetailResDto toNotificationDetailResDto(Notification notification) {
        return new NotificationDetailResDto(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.isRead(),
                notification.getCreatedAt());
    }
}