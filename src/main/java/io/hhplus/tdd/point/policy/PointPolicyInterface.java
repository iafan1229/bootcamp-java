package io.hhplus.tdd.point.policy;

import io.hhplus.tdd.point.domain.UserPoint;

public interface PointPolicyInterface {
    /**
     * 포인트 충전 정책을 검증합니다.
     *
     * @param userPoint 현재 사용자 포인트 정보
     * @param amount 충전하려는 금액
     * @throws IllegalArgumentException 정책에 위반되는 경우
     */
    void validateChargePolicy(UserPoint userPoint, long amount);

    /**
     * 포인트 사용 정책을 검증합니다.
     *
     * @param userPoint 현재 사용자 포인트 정보
     * @param amount 사용하려는 금액
     * @throws IllegalArgumentException 정책에 위반되는 경우
     */
    void validateUsePolicy(UserPoint userPoint, long amount);
}