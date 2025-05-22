package io.hhplus.tdd.point.api;

import io.hhplus.tdd.point.domain.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * 포인트 충전 API 테스트
 * - PATCH /point/{userId}/charge - 포인트 충전
 */
class PointChargeControllerTest extends PointControllerTestBase {

    @Test
    @DisplayName("PATCH /point/{userId}/charge - 포인트 충전 성공")
    void chargePoint_Success() throws Exception {
        // Given
        long userId = 1L;
        long chargeAmount = 15000L;
        UserPoint resultPoint = new UserPoint(userId, 15000L, System.currentTimeMillis());

        when(pointService.chargePoint(userId, chargeAmount)).thenReturn(resultPoint);
        PointController.PointRequest request = createPointRequest(chargeAmount);

        // When & Then
        mockMvc.perform(patch("/point/{userId}/charge", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(15000L))
                .andDo(print());

        verify(pointService).chargePoint(userId, chargeAmount);
    }

    @Test
    @DisplayName("PATCH /point/{userId}/charge - 0원 충전으로 400 에러")
    void chargePoint_ZeroAmount_BadRequest() throws Exception {
        // Given
        long userId = 1L;
        long invalidAmount = 0L;

        when(pointService.chargePoint(userId, invalidAmount))
                .thenThrow(new IllegalArgumentException("충전 금액은 0보다 커야 합니다."));

        PointController.PointRequest request = createPointRequest(invalidAmount);

        // When & Then
        mockMvc.perform(patch("/point/{userId}/charge", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(pointService).chargePoint(userId, invalidAmount);
    }

}