package io.hhplus.tdd.point.domain.policy;

import io.hhplus.tdd.point.domain.UserPoint;
import org.springframework.stereotype.Component;

@Component
public class ChargeAmountPolicy implements PointPolicyInterface {
    private static final long MAX_CHARGE_AMOUNT = 500_000;   // 최대 충전 금액
    private static final long MIN_CHARGE_AMOUNT = 1_000;     // 최소 충전 금액

    @Override
    public void validateChargePolicy(UserPoint userPoint, long amount) {
        if (amount < MIN_CHARGE_AMOUNT) {
            throw new IllegalArgumentException(
                    "최소 충전 금액은 " + MIN_CHARGE_AMOUNT + "포인트입니다.");
        }

        if (amount > MAX_CHARGE_AMOUNT) {
            throw new IllegalArgumentException(
                    "최대 충전 금액은 " + MAX_CHARGE_AMOUNT + "포인트입니다.");
        }
    }

    @Override
    public void validateUsePolicy(UserPoint userPoint, long amount) {
        // 사용 시에는 충전 한도 검증이 필요 없음
    }
}