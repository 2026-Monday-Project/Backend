package com.likelion.monday.domain.admin.dto;

import com.likelion.monday.domain.story.entity.StoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 관리자 검수 화면에 필요한 사연 전체 정보.
 * 공개 화면과 달리 작성자 이메일과 동의 현황까지 포함한다.
 */
@Schema(description = "관리자 사연 상세")
public record AdminStoryDetailResDto(

        @Schema(description = "사연 ID", example = "1")
        Long storyId,

        @Schema(description = "작성자 닉네임", example = "매기")
        String nickname,

        @Schema(description = "작성자 이메일", example = "monday@example.com")
        String email,

        @Schema(description = "반려동물 이름", example = "머고")
        String petName,

        @Schema(description = "반려동물 종류", example = "강아지")
        String petType,

        @Schema(description = "반려동물 나이", example = "3")
        Integer petAge,

        @Schema(description = "사연 제목", example = "우리 집 귀염둥이에게")
        String title,

        @Schema(description = "사연 본문")
        String content,

        @Schema(description = "사연 상태", example = "PENDING")
        StoryStatus status,

        @Schema(description = "공연 소개 후보 여부", example = "false")
        boolean performanceCandidate,

        @Schema(description = "조회 수", example = "128")
        int viewCount,

        @Schema(description = "공감 수", example = "12")
        int likeCount,

        @Schema(description = "[필수] 개인정보 수집·이용 동의")
        boolean privacyConsent,

        @Schema(description = "[필수] 콘텐츠 처리 및 운영정책 확인")
        boolean contentPolicyConsent,

        @Schema(description = "[필수] 웹사이트 공개 동의")
        boolean publicConsent,

        @Schema(description = "[선택] 공연 중 소개·낭독 동의")
        boolean introduceConsent,

        @Schema(description = "[선택] SNS·홍보물 활용 동의")
        boolean snsConsent,

        @Schema(description = "노출 순서대로 정렬된 사진 URL 목록")
        List<String> imageUrls,

        @Schema(description = "제출 일시")
        LocalDateTime createdAt,

        @Schema(description = "최종 수정 일시")
        LocalDateTime updatedAt
) {
}
