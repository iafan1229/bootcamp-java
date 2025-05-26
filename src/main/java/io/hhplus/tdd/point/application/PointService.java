package io.hhplus.tdd.point.application;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.common.PointSynchronizer;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.domain.policy.PointPolicy;
import io.hhplus.tdd.point.domain.TransactionType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService implements PointServiceInterface {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;
    private final PointPolicy pointPolicy;

    // 생성자 주입
    public PointService(UserPointTable userPointTable,
                        PointHistoryTable pointHistoryTable,
                        PointPolicy pointPolicy) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
        this.pointPolicy = pointPolicy;
    }

    /* 특정 유저의 포인트 정보를 조회한다.*/
    @Override
    public UserPoint getUserPoint(long id) {
        return userPointTable.selectById(id);
    }

    /* 특정 유저의 포인트 내역을 조회한다.*/
    @Override
    public List<PointHistory> getPointHistories(long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }

    /* 특정 유저의 포인트를 충전한다.*/
    @Override
    public UserPoint chargePoint(long id, long amount) {
        return PointSynchronizer.executeSynchronized(id, () -> {
            // 1. 현재 포인트 조회
            UserPoint currentPoint = userPointTable.selectById(id);

            // 2. 정책 검증
            pointPolicy.validateChargePolicy(currentPoint, amount);

            // 3. 충전 이력 추가
            pointHistoryTable.insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

            // 4. 포인트 업데이트 및 반환
            return userPointTable.insertOrUpdate(id, currentPoint.point() + amount);
        });
    }

    /* 특정 유저의 포인트를 사용한다.*/
    @Override
    public UserPoint usePoint(long id, long amount) {
        return PointSynchronizer.executeSynchronized(id, () -> {
            // 1. 현재 포인트 조회
            UserPoint currentPoint = userPointTable.selectById(id);

            // 2. 정책 검증
            pointPolicy.validateUsePolicy(currentPoint, amount);

            // 3. 사용 이력 추가
            pointHistoryTable.insert(id, amount, TransactionType.USE, System.currentTimeMillis());

            // 4. 포인트 업데이트 및 반환
            return userPointTable.insertOrUpdate(id, currentPoint.point() - amount);
        });
    }
}