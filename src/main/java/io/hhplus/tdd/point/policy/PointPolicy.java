package io.hhplus.tdd.point.policy;

import io.hhplus.tdd.point.domain.UserPoint;
import org.springframework.stereotype.Service;

@Service
public class PointPolicy {
    private final BasicPointPolicy basicPointPolicy;
    private final MaxBalancePointPolicy maxBalancePointPolicy;
    private final ChargeAmountPolicy chargeAmountPolicy;

    // 구체적인 정책들을 직접 주입받음
    public PointPolicy(BasicPointPolicy basicPointPolicy,
                       MaxBalancePointPolicy maxBalancePointPolicy,
                       ChargeAmountPolicy chargeAmountPolicy) {
        this.basicPointPolicy = basicPointPolicy;
        this.maxBalancePointPolicy = maxBalancePointPolicy;
        this.chargeAmountPolicy = chargeAmountPolicy;
    }

    public void validateChargePolicy(UserPoint userPoint, long amount) {
        basicPointPolicy.validateChargePolicy(userPoint, amount);
        maxBalancePointPolicy.validateChargePolicy(userPoint, amount);
        chargeAmountPolicy.validateChargePolicy(userPoint, amount);
    }

    public void validateUsePolicy(UserPoint userPoint, long amount) {
        basicPointPolicy.validateUsePolicy(userPoint, amount);
        maxBalancePointPolicy.validateUsePolicy(userPoint, amount);
        chargeAmountPolicy.validateUsePolicy(userPoint, amount);
    }
}