package io.hhplus.tdd.point.policy;

import io.hhplus.tdd.point.domain.UserPoint;
import java.util.ArrayList;
import java.util.List;

public class PointPolicy implements PointPolicyInterface {
    private final List<PointPolicyInterface> policies = new ArrayList<>();

    public PointPolicy() {
        // 기본 정책들 추가
        policies.add(new BasicPointPolicy());
        policies.add(new MaxBalancePointPolicy());
        policies.add(new ChargeAmountPolicy());
    }

    // 추가 정책을 동적으로 등록할 수 있는 메서드
    public void addPolicy(PointPolicy policy) {
        policies.add(policy);
    }

    @Override
    public void validateChargePolicy(UserPoint userPoint, long amount) {
        for (PointPolicyInterface policy : policies) {
            policy.validateChargePolicy(userPoint, amount);
        }
    }

    @Override
    public void validateUsePolicy(UserPoint userPoint, long amount) {
        for (PointPolicyInterface policy : policies) {
            policy.validateUsePolicy(userPoint, amount);
        }
    }
}