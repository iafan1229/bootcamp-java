
// PointUseControllerTest.java
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
 * 포인트 사용 API 테스트
 * - PATCH /point/{userId}/use - 포인트 사용
 */
class PointUseControllerTest extends PointControllerTestBase {

    @Test
    @DisplayName("PATCH /point/{userId}/use - 포인트 사용 성공")
    void usePoint_Success() throws Exception {
        // Given
        long userId = 1L;
        long useAmount = 3000L;
        UserPoint resultPoint = new UserPoint(userId, 2000L, System.currentTimeMillis());

        when(pointService.usePoint(userId, useAmount)).thenReturn(resultPoint);
        PointController.PointRequest request = createPointRequest(useAmount);

        // When & Then
        mockMvc.perform(patch("/point/{userId}/use", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(2000L))
                .andDo(print());

        verify(pointService).usePoint(userId, useAmount);
    }

    @Test
    @DisplayName("PATCH /point/{userId}/use - 잔액 부족으로 400 에러")
    void usePoint_InsufficientBalance_BadRequest() throws Exception {
        // Given
        long userId = 1L;
        long useAmount = 10000L;

        when(pointService.usePoint(userId, useAmount))
                .thenThrow(new IllegalArgumentException("포인트 잔액이 부족합니다. 현재 잔액: 5000"));

        PointController.PointRequest request = createPointRequest(useAmount);

        // When & Then
        mockMvc.perform(patch("/point/{userId}/use", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(pointService).usePoint(userId, useAmount);
    }

    @Test
    @DisplayName("PATCH /point/{userId}/use - 0원 사용으로 400 에러")
    void usePoint_ZeroAmount_BadRequest() throws Exception {
        // Given
        long userId = 1L;
        long invalidAmount = 0L;

        when(pointService.usePoint(userId, invalidAmount))
                .thenThrow(new IllegalArgumentException("사용 금액은 0보다 커야 합니다."));

        PointController.PointRequest request = createPointRequest(invalidAmount);

        // When & Then
        mockMvc.perform(patch("/point/{userId}/use", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
