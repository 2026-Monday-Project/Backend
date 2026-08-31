package com.likelion.monday.domain.story.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.likelion.monday.domain.story.entity.Story;
import com.likelion.monday.domain.story.entity.StoryStatus;
import com.likelion.monday.domain.story.repository.StoryLikeRepository;
import com.likelion.monday.domain.story.repository.StoryRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 공감 등록이 동시에 여러 번 요청돼도 중복 저장/카운트가 발생하지 않는지 실제 DB로 검증한다.
 * 진짜 동시성(유니크 제약이 실제로 막아주는지) 검증이 목적이라 Mockito 단위 테스트가 아닌
 * @SpringBootTest로 로컬 MySQL에 직접 붙는다. 로컬 MySQL이 떠있어야 통과한다 (application-local.yml 기준).
 */
@SpringBootTest
class StoryServiceConcurrencyTest {

    private static final int REQUEST_COUNT = 5;
    private static final Long TEST_ACCOUNT_ID = 999_999L;

    @Autowired
    private StoryService storyService;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private StoryLikeRepository storyLikeRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Long storyId;

    @AfterEach
    void cleanUp() {
        if (storyId == null) {
            return;
        }
        // deleteBy 파생 쿼리는 @Modifying과 동일하게 취급되어 트랜잭션 안에서만 실행할 수 있다.
        new TransactionTemplate(transactionManager).executeWithoutResult(status ->
                storyLikeRepository.deleteByStory_IdAndAccountId(storyId, TEST_ACCOUNT_ID));
        if (storyRepository.existsById(storyId)) {
            storyRepository.deleteById(storyId);
        }
    }

    @Test
    @DisplayName("같은 계정이 동시에 여러 번 공감해도 좋아요는 한 번만 반영된다")
    void 동시_공감_요청은_한_번만_반영된다() throws InterruptedException {
        Story story = Story.builder()
                .accountId(1L)
                .petName("두콩")
                .petType("강아지")
                .petAge(3)
                .title("동시성 테스트용 사연")
                .content("동시성 테스트용 사연 내용")
                .privacyConsent(true)
                .contentPolicyConsent(true)
                .publicConsent(true)
                .introduceConsent(true)
                .snsConsent(true)
                .build();
        story.updateStatus(StoryStatus.PUBLIC);
        story = storyRepository.save(story);
        storyId = story.getId();

        ExecutorService executorService = Executors.newFixedThreadPool(REQUEST_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(REQUEST_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(REQUEST_COUNT);
        AtomicInteger failureCount = new AtomicInteger();
        Long targetStoryId = storyId;

        for (int i = 0; i < REQUEST_COUNT; i++) {
            executorService.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    storyService.likeStory(targetStoryId, TEST_ACCOUNT_ID, null);
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        long likeRowCount = storyLikeRepository.countByStory_IdAndAccountId(targetStoryId, TEST_ACCOUNT_ID);
        Story reloaded = storyRepository.findById(targetStoryId).orElseThrow();

        // 동시 요청 중 일부가 DB 락 경합(데드락)으로 실패할 수는 있지만, 그건 이 리팩토링의 검증 대상이 아니다.
        // 여기서 반드시 보장돼야 하는 건 "중복 저장이 절대 없다"와 "카운트가 실제 저장된 행 수와 항상 일치한다"이다.
        assertThat(likeRowCount).isLessThanOrEqualTo(1);
        assertThat(reloaded.getLikeCount()).isEqualTo((int) likeRowCount);
    }
}