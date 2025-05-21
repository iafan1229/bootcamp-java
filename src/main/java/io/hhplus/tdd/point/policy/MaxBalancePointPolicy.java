package io.hhplus.tdd.point.policy;

import io.hhplus.tdd.point.domain.UserPoint;

public class MaxBalancePointPolicy implements PointPolicyInterface {
    private static final long MAX_POINT_BALANCE = 1_000_000; // 백만 포인트

    @Override
    public void validateChargePolicy(UserPoint userPoint, long amount) {
        if (userPoint.point() + amount > MAX_POINT_BALANCE) {
            throw new IllegalArgumentException(
                    "최대 보유 가능 포인트는 " + MAX_POINT_BALANCE + "포인트입니다.");
        }
    }

    @Override
    public void validateUsePolicy(UserPoint userPoint, long amount) {
        // 사용 시에는 최대 잔고 검증이 필요 없음
    }
}