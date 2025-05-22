package io.hhplus.tdd.point.api;
import io.hhplus.tdd.point.application.PointService;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;

/**
 * 포인트 동시성 제어 테스트
 * - 서로 다른 사용자들의 동시 요청이 올바르게 처리되는지 검증
 * - 대기 하다 순서 대로 처리 되어야 함.
 */
@SpringBootTest
class PointConcurrencyTest {

    @Autowired
    private PointService pointService;

    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        executorService = Executors.newFixedThreadPool(10);
    }

    @Test
    @DisplayName("서로 다른 사용자의 동시 충전 요청 - 모두 성공해야 함")
    void UserMultipleRequestSynchronized() throws Exception {
        // Given
        int userCount = 5;
        long chargeAmount = 10000L;
        List<Long> userIds = List.of(1L, 2L, 3L, 4L, 5L);

        // When: 서로 다른 사용자들에 대해 동시에 충전 요청
        List<Future<UserPoint>> futures = new ArrayList<>();
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch readyLatch = new CountDownLatch(userCount);

        for (Long userId : userIds) {
            Future<UserPoint> future = executorService.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await(); // 모든 스레드가 동시에 시작하도록 대기
                    return pointService.chargePoint(userId, chargeAmount);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
            futures.add(future);
        }

        readyLatch.await(); // 모든 스레드가 준비될 때까지 대기
        startLatch.countDown(); // 모든 스레드 동시 시작

        // Then: 모든 작업이 성공해야 함
        List<UserPoint> results = new ArrayList<>();
        for (Future<UserPoint> future : futures) {
            UserPoint result = future.get(5, TimeUnit.SECONDS);
            results.add(result);
        }

        // 모든 사용자의 포인트가 정상적으로 충전되었는지 확인
        assertThat(results).hasSize(userCount);
        assertThat(results).allMatch(userPoint -> userPoint.point() == chargeAmount);

        // 각 사용자별로 최종 포인트와 이력 확인
        for (Long userId : userIds) {
            UserPoint finalPoint = pointService.getUserPoint(userId);
            assertThat(finalPoint.point()).isEqualTo(chargeAmount);

            List<PointHistory> histories = pointService.getPointHistories(userId);
            assertThat(histories).hasSize(1);
            assertThat(histories.get(0).type()).isEqualTo(TransactionType.CHARGE);
            assertThat(histories.get(0).amount()).isEqualTo(chargeAmount);
            assertThat(histories.get(0).userId()).isEqualTo(userId);
        }
    }
}