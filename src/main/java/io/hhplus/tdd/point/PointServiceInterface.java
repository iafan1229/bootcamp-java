package io.hhplus.tdd.point;
import java.util.List;

public interface PointServiceInterface {
    /**
     * 특정 유저의 포인트 정보를 조회합니다.
     *
     * @param id 조회할 유저의 ID
     */
    UserPoint getUserPoint(long id);

    /**
     * 특정 유저의 포인트 사용/충전 내역을 조회합니다.
     *
     * @param id 조회할 유저의 ID
     */
    List<PointHistory> getPointHistories(long id);

    /**
     * 특정 유저의 포인트를 충전합니다.
     *
     * @param id 포인트를 충전할 유저의 ID
     * @param amount 충전할 포인트 양
     */
    UserPoint chargePoint(long id, long amount);

    /**
     * 특정 유저의 포인트를 사용합니다.
     *
     * @param id 포인트를 사용할 유저의 ID
     * @param amount 사용할 포인트 양
     **/
    UserPoint usePoint(long id, long amount);
}

