package io.hhplus.tdd.point.api;

import io.hhplus.tdd.point.application.PointService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * PointController 테스트를 위한 공통 베이스 클래스
 * - 공통 설정과 헬퍼 메서드 제공
 */
@WebMvcTest(PointController.class)
public abstract class PointControllerTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected PointService pointService;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // 공통 설정이 있다면 여기에
    }

    // 공통 헬퍼 메서드들
    protected String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    protected PointController.PointRequest createPointRequest(long amount) {
        return new PointController.PointRequest(amount);
    }
}