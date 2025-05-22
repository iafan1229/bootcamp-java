
// =================== 2. 조회 API 테스트 ===================

// PointGetControllerTest.java
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * 포인트 조회 API 테스트
 * - GET /point/{userId} - 포인트 조회
 * - GET /point/{userId}/histories - 포인트 내역 조회
 */
class PointGetControllerTest extends PointControllerTestBase {
    @Test
    @DisplayName("GET /point/{userId} - 포인트 조회 성공")
    void getUserPoint_Success() throws Exception {
        // Given
        long userId = 1L;
        UserPoint userPoint = new UserPoint(userId, 0L, System.currentTimeMillis());
        when(pointService.getUserPoint(userId)).thenReturn(userPoint);

        // When & Then
        mockMvc.perform(get("/point/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(0L))
                .andExpect(jsonPath("$.updateMillis").exists())
                .andDo(print());

        verify(pointService).getUserPoint(userId);
    }


    @Test
    @DisplayName("GET /point/{userId} - 신규 사용자 조회 (0포인트)")
    void getUserPoint_NewUser() throws Exception {
        // Given
        long userId = 1L;
        UserPoint newUserPoint = UserPoint.empty(userId);
        when(pointService.getUserPoint(userId)).thenReturn(newUserPoint);

        // When & Then
        mockMvc.perform(get("/point/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(0L))
                .andDo(print());
    }

    @Test
    @DisplayName("GET /point/{userId} - 잘못된 사용자 ID 형식")
    void getUserPoint_InvalidUserIdFormat() throws Exception {
        // Given
        String invalidUserId = "invalid";

        // When & Then
        mockMvc.perform(get("/point/{userId}", invalidUserId))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(pointService, never()).getUserPoint(anyLong());
    }

}