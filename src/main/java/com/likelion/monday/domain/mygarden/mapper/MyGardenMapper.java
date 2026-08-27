package com.likelion.monday.domain.mygarden.mapper;

import com.likelion.monday.domain.mygarden.dto.MyStorySummaryResDto;
import com.likelion.monday.domain.story.entity.Story;
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
}