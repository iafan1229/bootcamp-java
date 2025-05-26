package io.hhplus.tdd.point.domain.policy;

import io.hhplus.tdd.point.domain.UserPoint;
import org.springframework.stereotype.Component;

@Component
public class BasicPointPolicy implements PointPolicyInterface {
    @Override
    public void validateChargePolicy(UserPoint userPoint, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
    }

    @Override
    public void validateUsePolicy(UserPoint userPoint, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("사용 금액은 0보다 커야 합니다.");
        }

        if (userPoint.point() < amount) {
            throw new IllegalArgumentException(
                    "포인트 잔액이 부족합니다. 현재 잔액: " + userPoint.point());
        }
    }
}
