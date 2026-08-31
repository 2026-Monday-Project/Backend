package com.likelion.monday.domain.story.mapper;

import com.likelion.monday.domain.story.dto.StoryCardResDto;
import com.likelion.monday.domain.story.dto.StoryCreateReqDto;
import com.likelion.monday.domain.story.dto.StoryImageResDto;
import com.likelion.monday.domain.story.dto.StoryWriteResDto;
import com.likelion.monday.domain.story.entity.Story;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StoryMapper {

    public Story toEntity(StoryCreateReqDto request, Long accountId) {
        return Story.builder()
                .accountId(accountId)
                .petName(request.petName())
                .petType(request.petType())
                .petAge(request.petAge())
                .title(request.title())
                .content(request.content())
                .privacyConsent(request.privacyConsent())
                .contentPolicyConsent(request.contentPolicyConsent())
                .publicConsent(request.publicConsent())
                .introduceConsent(request.introduceConsent())
                .snsConsent(request.snsConsent())
                .build();
    }

    public StoryWriteResDto toWriteResDto(Story story, String nickname, List<StoryImageResDto> images) {
        return new StoryWriteResDto(story.getId(), nickname, story.getStatus(), images);
    }

    public StoryCardResDto toCardResDto(Story story, String nickname, String thumbnailUrl) {
        return new StoryCardResDto(
                story.getId(),
                thumbnailUrl,
                story.getPetName(),
                story.getTitle(),
                nickname,
                story.getCreatedAt(),
                story.getViewCount(),
                story.getLikeCount());
    }
}
