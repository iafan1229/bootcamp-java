package io.hhplus.tdd.point.domain.policy;

import io.hhplus.tdd.point.domain.UserPoint;

/**
 * 포인트 정책 검증을 위한 인터페이스
 * 각각의 구체적인 정책 클래스들이 이 인터페이스를 구현합니다.
 */
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