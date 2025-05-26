package io.hhplus.tdd.point.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * 포인트 작업에 대한 동기화를 관리하는 클래스
 */
public class PointSynchronizer {
    // 사용자 ID별 락 관리
    private static final Map<Long, Lock> USER_LOCKS = new ConcurrentHashMap<>();

    /**
     * 동기화된 블록 내에서 작업을 실행
     *
     * @param userId 사용자 ID
     * @param operation 실행할 작업
     * @return 작업 결과
     */
    public static <T> T executeSynchronized(long userId, Supplier<T> operation) {
        Lock lock = getUserLock(userId);
        lock.lock();
        try {
            return operation.get();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 사용자별 락 객체 반환 (없으면 새로 생성)
     */
    private static Lock getUserLock(long userId) {
        return USER_LOCKS.computeIfAbsent(userId, k -> new ReentrantLock());
    }
}