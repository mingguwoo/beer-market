package com.sh.beer.market.common.lock;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * @author
 * @date 2023/7/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLocker {

    private static final long DEFAULT_MAX_LOCK_WAIT_SECOND = 5;

    private final RedisClient redisClient;

    public <T> T executeWithLock(String key, Long maxLockTime, Supplier<T> task) {
        /*RLock lock = redisClient.getRLock(key);
        try {
            if (lock.tryLock(maxLockTime != null ? maxLockTime : DEFAULT_MAX_LOCK_WAIT_SECOND, TimeUnit.SECONDS)) {
                return task.get();
            } else {
                throw new BusinessException(ConfigErrorCode.LOCK_FAILED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("RedissonLocker executeWithLock interrupted", e);
            throw new BusinessException(ConfigErrorCode.LOCK_FAILED);
        } finally {
            if (lock.isHeldByCurrentThread() && lock.isLocked()) {
                lock.unlock();
            }
        }*/
        return null;
    }

}
