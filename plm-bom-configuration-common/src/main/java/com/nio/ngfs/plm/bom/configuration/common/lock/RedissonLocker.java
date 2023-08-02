package com.nio.ngfs.plm.bom.configuration.common.lock;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author xiaozhou.tu
 * @date 2023/7/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLocker {

    private static final long MAX_LOCK_WAIT_SECOND = 5;

    private final RedisClient redisClient;

    public <T> T executeWithLock(String key, Supplier<T> task) {
        RLock lock = redisClient.getRLock(key);
        try {
            if (lock.tryLock(MAX_LOCK_WAIT_SECOND, TimeUnit.SECONDS)) {
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
        }
    }

}
