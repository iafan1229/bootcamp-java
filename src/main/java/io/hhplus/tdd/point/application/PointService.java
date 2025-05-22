package io.hhplus.tdd.point.application;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.PointSynchronizer;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.policy.PointPolicy;
import io.hhplus.tdd.point.domain.TransactionType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService implements PointServiceInterface {

    // 의존성 주입을 위한 필드 추가
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    // 생성자 주입
    public PointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    @Override
    public UserPoint getUserPoint(long id) {
        return userPointTable.selectById(id); // 주입받은 인스턴스 사용
    }

    @Override
    public List<PointHistory> getPointHistories(long id) {
        return pointHistoryTable.selectAllByUserId(id); // 주입받은 인스턴스 사용
    }

    @Override
    public UserPoint chargePoint(long id, long amount) {
        return PointSynchronizer.executeSynchronized(id, () -> {
            // 1. 현재 포인트 조회
            UserPoint currentPoint = userPointTable.selectById(id);

            // 2. 정책 검증
            new PointPolicy().validateChargePolicy(currentPoint, amount);

            // 3. 충전 이력 추가
            pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

            // 4. 포인트 업데이트 및 반환
            return userPointTable.insertOrUpdate(id, currentPoint.point() + amount); // 기존 포인트에 추가
        });
    }

    @Override
    public UserPoint usePoint(long id, long amount) {
        return PointSynchronizer.executeSynchronized(id, () -> {
            // 1. 현재 포인트 조회
            UserPoint currentPoint = userPointTable.selectById(id);

            // 2. 정책 검증
            new PointPolicy().validateUsePolicy(currentPoint, amount);

            // 3. 사용 이력 추가
            pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());

            // 4. 포인트 업데이트 및 반환
            return userPointTable.insertOrUpdate(id, currentPoint.point() - amount); // 기존 포인트에서 차감
        });
    }
}