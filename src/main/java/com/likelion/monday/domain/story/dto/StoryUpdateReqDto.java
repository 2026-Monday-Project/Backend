package com.likelion.monday.domain.story.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 사연 수정 요청.
 * 기존 사진 중 남길 것을 keepImageIds로 지정하고, 새로 추가할 사진만 multipart의 images 파트로 보낸다.
 * keepImageIds에 없는 기존 사진은 삭제되며, 빈 배열을 보내면 기존 사진이 모두 지워진다.
 */
@Schema(description = "사연 수정 요청")
public record StoryUpdateReqDto(

        /*
         * 로그인 기능이 붙기 전까지 사용하는 임시 소유자 확인 수단이다.
         * 인증이 도입되면 이 필드를 지우고 인증 정보에서 계정을 꺼내도록 바꾼다.
         */
        @Schema(description = "사연 작성 시 입력한 이메일 (본인 확인용)", example = "monday@example.com")
        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
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

        @Schema(description = "유지할 기존 사진 ID 목록. 생략하면 기존 사진을 모두 삭제한다.")
        List<Long> keepImageIds,

        @Schema(description = "[선택] 공연 중 소개·낭독 동의")
        boolean introduceConsent,

        @Schema(description = "[선택] SNS·홍보물 활용 동의")
        boolean snsConsent
) {
}
