package io.hhplus.tdd.point;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;

import java.util.List;


public class PointService implements PointServiceInterface {

    /* 특정 유저의 포인트 정보를 조회한다.*/
    @Override
    public UserPoint getUserPoint(long id){
       return new UserPointTable().selectById(id);
    }

    /* 특정 유저의 포인트 내역을 조회한다.*/
    @Override
    public List<PointHistory> getPointHistories(long id){
        return new PointHistoryTable().selectAllByUserId(id);
    }

    /* 특정 유저의 포인트를 충전한다.*/
    @Override
    public UserPoint chargePoint(long id, long amount){
        if(amount <= 0){
            throw new IllegalArgumentException("충전 금액이 유효하지 않습니다.");
        }
        return new UserPointTable().insertOrUpdate(id, amount);
    }

    /* 특정 유저의 포인트를 사용한다.*/
    @Override
    public UserPoint usePoint(long id, long amount){
        if(amount <= 0){
            throw new IllegalArgumentException("사용 금액이 유효하지 않습니다.");
        }
        return new UserPointTable().insertOrUpdate(id, -amount);
    }
}
