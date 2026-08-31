package com.likelion.monday.domain.story.dto;

import com.likelion.monday.domain.story.entity.StoryImage;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 사연에 첨부된 사진 한 장.
 * 사연을 수정할 때 남길 사진을 keepImageIds로 지정해야 하므로 URL과 함께 ID를 내려준다.
 */
@Schema(description = "사연 첨부 사진")
public record StoryImageResDto(

        @Schema(description = "사진 ID. 사연 수정 시 keepImageIds에 넣는 값이다.", example = "1")
        Long imageId,

        @Schema(description = "사진 URL", example = "/images/story/8f3c1e2a-....jpg")
        String imageUrl
) {

    public static StoryImageResDto from(StoryImage storyImage) {
        return new StoryImageResDto(storyImage.getId(), storyImage.getImageUrl());
    }
}
