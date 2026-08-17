package com.likelion.monday.domain.story.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 사연 작성 STEP 1~3에서 입력한 값을 한 번에 받는다.
 * 사진은 multipart의 images 파트로 따로 전달되며, 제출 시점에 일괄 업로드된다.
 */
@Schema(description = "사연 작성 요청")
public record StoryCreateReqDto(

        @Schema(description = "공개될 닉네임", example = "매기")
        @NotBlank(message = "닉네임을 입력해 주세요.")
        @Pattern(regexp = "^[가-힣]{1,10}$", message = "닉네임은 한글 10자 이내로 입력해 주세요.")
        String nickname,

        @Schema(description = "검토 결과를 안내받을 이메일", example = "monday@example.com")
        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Size(max = 100, message = "이메일은 100자 이내로 입력해 주세요.")
        String email,

        @Schema(description = "반려동물 이름", example = "머고")
        @NotBlank(message = "반려동물 이름을 입력해 주세요.")
        @Size(max = 50, message = "반려동물 이름은 50자 이내로 입력해 주세요.")
        String petName,

        @Schema(description = "반려동물 종류", example = "강아지")
        @NotBlank(message = "반려동물 종류를 입력해 주세요.")
        @Size(max = 50, message = "반려동물 종류는 50자 이내로 입력해 주세요.")
        String petType,

        @Schema(description = "반려동물 나이", example = "3")
        @NotNull(message = "반려동물 나이를 입력해 주세요.")
        @Min(value = 0, message = "반려동물 나이는 0살 이상이어야 합니다.")
        @Max(value = 100, message = "반려동물 나이는 100살 이하여야 합니다.")
        Integer petAge,

        @Schema(description = "사연 제목", example = "우리 집 귀염둥이에게")
        @NotBlank(message = "사연 제목을 입력해 주세요.")
        @Size(max = 100, message = "사연 제목은 100자 이내로 입력해 주세요.")
        String title,

        @Schema(description = "사연 본문")
        @NotBlank(message = "사연 내용을 입력해 주세요.")
        @Size(max = 5000, message = "사연 내용은 5000자 이내로 입력해 주세요.")
        String content,

        @Schema(description = "[필수] 개인정보 수집·이용 동의")
        @AssertTrue(message = "개인정보 수집·이용에 동의해 주세요.")
        boolean privacyConsent,

        @Schema(description = "[필수] 콘텐츠 처리 및 운영정책 확인")
        @AssertTrue(message = "콘텐츠 처리 및 운영정책에 동의해 주세요.")
        boolean contentPolicyConsent,

        @Schema(description = "[필수] 웹사이트 공개 동의")
        @AssertTrue(message = "웹사이트 공개에 동의해 주세요.")
        boolean publicConsent,

        @Schema(description = "[선택] 공연 중 소개·낭독 동의")
        boolean introduceConsent,

        @Schema(description = "[선택] SNS·홍보물 활용 동의")
        boolean snsConsent
) {
}
