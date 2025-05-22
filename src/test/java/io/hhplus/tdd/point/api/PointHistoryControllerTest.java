package io.hhplus.tdd.point.api;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * 포인트 내역 조회 API 테스트
 * - GET /point/{userId}/histories - 포인트 내역 조회
 */
class PointHistoryControllerTest extends PointControllerTestBase {

    @Test
    @DisplayName("GET /point/{userId}/histories - 포인트 내역 조회 성공")
    void getPointHistories_Success() throws Exception {
        // Given
        long userId = 1L;
        List<PointHistory> histories = List.of(
                new PointHistory(1L, userId, 1000L, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, userId, 500L, TransactionType.USE, System.currentTimeMillis())
        );

        when(pointService.getPointHistories(userId)).thenReturn(histories);

        // When & Then
        mockMvc.perform(get("/point/{userId}/histories", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].amount").value(1000L))
                .andExpect(jsonPath("$[0].type").value("CHARGE"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userId").value(userId))
                .andExpect(jsonPath("$[1].amount").value(500L))
                .andExpect(jsonPath("$[1].type").value("USE"))
                .andDo(print());

        verify(pointService).getPointHistories(userId);
    }

    @Test
    @DisplayName("GET /point/{userId}/histories - 거래 내역이 없는 사용자 (빈 리스트)")
    void getPointHistories_EmptyList() throws Exception {
        // Given
        long userId = 999L;
        when(pointService.getPointHistories(userId)).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/point/{userId}/histories", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(pointService).getPointHistories(userId);
    }

    @Test
    @DisplayName("GET /point/{userId}/histories - 잘못된 사용자 ID 형식")
    void getPointHistories_InvalidUserIdFormat() throws Exception {
        // Given
        String invalidUserId = "invalid";

        // When & Then
        mockMvc.perform(get("/point/{userId}/histories", invalidUserId))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(pointService, never()).getPointHistories(anyLong());
    }
}