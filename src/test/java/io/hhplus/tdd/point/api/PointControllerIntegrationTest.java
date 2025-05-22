
// PointControllerIntegrationTest.java
package io.hhplus.tdd.point.api;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.domain.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * PointController API 통합 시나리오 테스트
 * - 여러 API를 조합한 전체 플로우 테스트
 */
class PointControllerIntegrationTest extends PointControllerTestBase {

    @Test
    @DisplayName("API 통합 시나리오 - 충전 → 사용 → 내역 조회")
    void integrationScenario_ChargeUseAndCheckHistory() throws Exception {
        long userId = 1L;

        // 1. 포인트 충전
        UserPoint afterCharge = new UserPoint(userId, 10000L, System.currentTimeMillis());
        when(pointService.chargePoint(userId, 10000L)).thenReturn(afterCharge);

        PointController.PointRequest chargeRequest = createPointRequest(10000L);
        mockMvc.perform(patch("/point/{userId}/charge", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(chargeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(10000L));

        // 2. 포인트 사용
        UserPoint afterUse = new UserPoint(userId, 7000L, System.currentTimeMillis());
        when(pointService.usePoint(userId, 3000L)).thenReturn(afterUse);

        PointController.PointRequest useRequest = createPointRequest(3000L);
        mockMvc.perform(patch("/point/{userId}/use", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(useRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(7000L));

        // 3. 포인트 내역 조회
        List<PointHistory> histories = List.of(
                new PointHistory(1L, userId, 10000L, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, userId, 3000L, TransactionType.USE, System.currentTimeMillis())
        );
        when(pointService.getPointHistories(userId)).thenReturn(histories);

        mockMvc.perform(get("/point/{userId}/histories", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        // 4. 최종 포인트 조회
        when(pointService.getUserPoint(userId)).thenReturn(afterUse);

        mockMvc.perform(get("/point/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(7000L));

        // 모든 서비스 메서드 호출 검증
        verify(pointService).chargePoint(userId, 10000L);
        verify(pointService).usePoint(userId, 3000L);
        verify(pointService).getPointHistories(userId);
        verify(pointService).getUserPoint(userId);
    }
}