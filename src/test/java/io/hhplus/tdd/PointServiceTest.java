package io.hhplus.tdd;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.application.PointService;
import io.hhplus.tdd.point.domain.UserPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PointServiceTest {
    private UserPointTable userPointTable;
    private PointHistoryTable pointHistoryTable;
    private PointService pointService;

    @BeforeEach
    void setUp() {
        // PointService에 테스트용 생성자가 없으므로 기본 생성자 사용
        pointService = new PointService();

        // 참고: 현재는 PointService 내부에서 새로운 Table 인스턴스를 생성함
        // 따라서 userPointTable, pointHistoryTable 변수는 사용하지 않음
    }

    // =================== 1. 포인트 조회 테스트 ===================

    @Test
    @DisplayName("신규 사용자 포인트 조회 - 0포인트 반환")
    void getUserPoint_NewUser_ReturnsZeroPoint() {
        // Given - 빈 데이터베이스, 신규 사용자
        long newUserId = 1L;

        // When - 포인트 조회
        UserPoint result = pointService.getUserPoint(newUserId);

        // Then - 새 사용자는 0포인트여야 함
        assertNotNull(result);
        assertEquals(newUserId, result.id());
        assertEquals(0L, result.point());
    }

    @Test
    @DisplayName("다른 사용자 ID로 포인트 조회 - 모두 0포인트로 시작")
    void getUserPoint_DifferentUserIds() {
        // Given - 여러 다른 사용자 ID들
        long userId1 = 1L;
        long userId2 = 999L;
        long userId3 = 12345L;

        // When - 각각 조회
        UserPoint result1 = pointService.getUserPoint(userId1);
        UserPoint result2 = pointService.getUserPoint(userId2);
        UserPoint result3 = pointService.getUserPoint(userId3);

        // Then - 모든 사용자가 0포인트에서 시작
        assertEquals(userId1, result1.id());
        assertEquals(0L, result1.point());

        assertEquals(userId2, result2.id());
        assertEquals(0L, result2.point());

        assertEquals(userId3, result3.id());
        assertEquals(0L, result3.point());
    }

}
