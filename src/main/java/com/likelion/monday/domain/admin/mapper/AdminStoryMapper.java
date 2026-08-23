package com.likelion.monday.domain.admin.mapper;

import com.likelion.monday.domain.account.entity.Account;
import com.likelion.monday.domain.admin.dto.AdminStoryDetailResDto;
import com.likelion.monday.domain.admin.dto.AdminStorySummaryResDto;
import com.likelion.monday.domain.story.entity.Story;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AdminStoryMapper {

    public AdminStorySummaryResDto toSummaryResDto(Story story, String nickname, String thumbnailUrl) {
        return new AdminStorySummaryResDto(
                story.getId(),
                story.getTitle(),
                nickname,
                story.getPetName(),
                story.getStatus(),
                thumbnailUrl,
                story.getCreatedAt());
    }

    public AdminStoryDetailResDto toDetailResDto(Story story, Account account, List<String> imageUrls) {
        return new AdminStoryDetailResDto(
                story.getId(),
                account.getNickname(),
                account.getEmail(),
                story.getPetName(),
                story.getPetType(),
                story.getPetAge(),
                story.getTitle(),
                story.getContent(),
                story.getStatus(),
                story.isPerformanceCandidate(),
                story.getViewCount(),
                story.getLikeCount(),
                story.isPrivacyConsent(),
                story.isContentPolicyConsent(),
                story.isPublicConsent(),
                story.isIntroduceConsent(),
                story.isSnsConsent(),
                imageUrls,
                story.getCreatedAt(),
                story.getUpdatedAt());
    }
}
