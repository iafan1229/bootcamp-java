package io.hhplus.tdd.point.api;

import io.hhplus.tdd.point.application.PointService;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 포인트 관련 API를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/point")
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);

    private final PointService pointService; // 👈 외부에서 받을 준비

    // 생성자를 통해 PointService를 받음 (생성자 주입)
    public PointController(PointService pointService) {
        this.pointService = pointService; // 👈 외부에서 넣어줌!
    }

    /**
     * 특정 유저의 포인트를 조회한다.
     *
     * @param userId 조회할 유저의 ID
     * @return 유저의 포인트 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserPoint> getUserPoint(@PathVariable long userId) {
        // 받아둔 PointService 사용
        UserPoint userPoint = pointService.getUserPoint(userId); // 👈 주입받은 것 사용!
        return ResponseEntity.ok(userPoint);
    }
    /**
     * 특정 유저의 포인트 충전/이용 내역을 조회한다.
     *
     * @param userId 조회할 유저의 ID
     * @return 유저의 포인트 이력 목록
     */
    @GetMapping("/{userId}/histories")
    public ResponseEntity<List<PointHistory>> getPointHistories(@PathVariable long userId) {
        log.info("포인트 이력 조회 요청 - 유저 ID: {}", userId);
        List<PointHistory> histories = pointService.getPointHistories(userId);
        return ResponseEntity.ok(histories);
    }

    /**
     * 특정 유저의 포인트를 충전한다.
     *
     * @param userId 충전할 유저의 ID
     * @param request 충전 요청 정보
     * @return 충전 후 유저의 포인트 정보
     */
    @PatchMapping("/{userId}/charge")
    public ResponseEntity<UserPoint> chargePoint(
            @PathVariable long userId,
            @RequestBody PointRequest request
    ) {
        log.info("포인트 충전 요청 - 유저 ID: {}, 금액: {}", userId, request.getAmount());

        try {
            UserPoint userPoint = pointService.chargePoint(userId, request.getAmount());
            return ResponseEntity.ok(userPoint);
        } catch (IllegalArgumentException e) {
            log.error("포인트 충전 오류 - 유저 ID: {}, 금액: {}, 오류: {}",
                    userId, request.getAmount(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 특정 유저의 포인트를 사용한다.
     *
     * @param userId 사용할 유저의 ID
     * @param request 사용 요청 정보
     * @return 사용 후 유저의 포인트 정보
     */
    @PatchMapping("/{userId}/use")
    public ResponseEntity<UserPoint> usePoint(
            @PathVariable long userId,
            @RequestBody PointRequest request
    ) {
        log.info("포인트 사용 요청 - 유저 ID: {}, 금액: {}", userId, request.getAmount());

        try {
            UserPoint userPoint = pointService.usePoint(userId, request.getAmount());
            return ResponseEntity.ok(userPoint);
        } catch (IllegalArgumentException e) {
            log.error("포인트 사용 오류 - 유저 ID: {}, 금액: {}, 오류: {}",
                    userId, request.getAmount(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 포인트 요청 객체 (DTO)
     */
    public static class PointRequest {
        private long amount;

        // 기본 생성자 (JSON 역직렬화에 필요)
        public PointRequest() {
        }

        public PointRequest(long amount) {
            this.amount = amount;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }
    }
}