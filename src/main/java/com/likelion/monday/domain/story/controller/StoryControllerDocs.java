package com.likelion.monday.domain.story.controller;

import com.likelion.monday.domain.story.constant.StorySort;
import com.likelion.monday.domain.story.dto.StoryCreateReqDto;
import com.likelion.monday.domain.story.dto.StoryDetailResDto;
import com.likelion.monday.domain.story.dto.StoryPageResDto;
import com.likelion.monday.domain.story.dto.StoryUpdateReqDto;
import com.likelion.monday.domain.story.dto.StoryWriteResDto;
import com.likelion.monday.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Story", description = "사연 작성/수정 API")
@RequestMapping("/stories")
public interface StoryControllerDocs {

    @Operation(
            summary = "사연 작성",
            description = """
                    사연 작성 STEP 1~3에서 입력한 값과 사진을 한 번에 제출한다.
                    multipart/form-data로 request(JSON) 파트와 images(파일) 파트를 함께 보낸다.
                    사진은 최대 5장까지 첨부할 수 있고, 제출 시점에 일괄 업로드된다.
                    처음 보는 이메일이면 계정이 생성되고, 이미 있는 이메일이면 그 계정에 사연이 추가된다.
                    제출된 사연은 운영진 검토 전이므로 PENDING(검토중) 상태로 저장된다.
                    """
    )
    ApiResponse<StoryWriteResDto> createStory(StoryCreateReqDto request, List<MultipartFile> images);

    @Operation(
            summary = "사연 수정",
            description = """
                    검토중(PENDING) 상태인 본인 사연만 수정할 수 있다.
                    남길 기존 사진은 keepImageIds로 지정하고, 새로 추가할 사진만 images 파트로 보낸다.
                    keepImageIds에 없는 기존 사진은 삭제되며, 남긴 사진 뒤로 새 사진이 이어 붙는다.
                    로그인이 필요하며, 토큰의 계정과 사연 작성자가 다르면 수정할 수 없다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ApiResponse<StoryWriteResDto> updateStory(@Parameter(hidden = true) Long accountId, Long storyId,
                                              StoryUpdateReqDto request, List<MultipartFile> images);

    @Operation(
            summary = "사연 목록 조회",
            description = """
                    공개(PUBLIC)된 사연만 카드 형태로 페이지네이션 조회한다.
                    정렬 기준은 LATEST(최신순, 기본값) · VIEWS(조회순) · LIKES(공감순) 중에서 선택한다.
                    본문은 목록에 노출하지 않는다.
                    """
    )
    ApiResponse<StoryPageResDto> getStories(StorySort sort, int page, int size);

    @Operation(
            summary = "사연 상세 조회",
            description = """
                    공개(PUBLIC)된 사연 하나를 상세로 조회한다. 본문과 전체 사진 목록을 포함한다.
                    응답과 함께 게스트 식별용 쿠키(guest_key)가 발급되며, 이후 같은 브라우저로 다시 조회해도 조회수에 반영되지 않는다.
                    검토중이거나 비공개인 사연은 404로 처리한다.
                    """
    )
    ApiResponse<StoryDetailResDto> getStory(Long storyId, HttpServletRequest request, HttpServletResponse response);
}
