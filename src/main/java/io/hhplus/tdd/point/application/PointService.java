package io.hhplus.tdd.point.application;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.policy.PointPolicy;
import io.hhplus.tdd.point.policy.PointPolicyInterface;
import io.hhplus.tdd.point.domain.TransactionType;

import java.util.List;

public class PointService implements PointServiceInterface {


    /* 특정 유저의 포인트 정보를 조회한다.*/
    @Override
    public UserPoint getUserPoint(long id){
        return new UserPointTable().selectById(id); // 주입받은 인스턴스 사용
    }

    /* 특정 유저의 포인트 내역을 조회한다.*/
    @Override
    public List<PointHistory> getPointHistories(long id){
        return new PointHistoryTable().selectAllByUserId(id); // 주입받은 인스턴스 사용
    }

    /* 특정 유저의 포인트를 충전한다.*/
    @Override
    public UserPoint chargePoint(long id, long amount) {
        // 1. 현재 포인트 조회
        UserPoint currentPoint = new UserPointTable().selectById(id);

        // 2. 정책 검증
        new PointPolicy().validateChargePolicy(currentPoint, amount);

        // 3. 충전 이력 추가
        new PointHistoryTable().insert(id, amount, TransactionType.CHARGE, System.currentTimeMillis());

        // 4. 포인트 업데이트 및 반환
        return new UserPointTable().insertOrUpdate(id, amount);
    }

    /* 특정 유저의 포인트를 사용한다.*/
    @Override
    public UserPoint usePoint(long id, long amount){
        // 1. 현재 포인트 조회
        UserPoint currentPoint = new UserPointTable().selectById(id);

        // 2. 정책 검증
        new PointPolicy().validateUsePolicy(currentPoint, amount);

        // 3. 사용 이력 추가
        new PointHistoryTable().insert(id, amount, TransactionType.USE, System.currentTimeMillis());

        // 4. 포인트 업데이트 및 반환
        return new UserPointTable().insertOrUpdate(id, -amount);
    }
}