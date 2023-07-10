package com.nio.ngfs.plm.bom.configuration.common.lock;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author xiaozhou.tu
 * @date 2023/7/10
 */
@Component
public class RedissonLocker {

    private static final long MAX_LOCK_WAIT_SECOND = 3;

    @Resource
    private RedissonClient redissonClient;

    public <T> T executeWithLock(String key, Supplier<T> task) {
        RLock lock = redissonClient.getLock(key);
        try {
            if (lock.tryLock(MAX_LOCK_WAIT_SECOND, TimeUnit.SECONDS)) {
                return task.get();
            } else {
                throw new BusinessException(ConfigErrorCode.LOCK_FAILED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ConfigErrorCode.LOCK_FAILED);
        } finally {
            if (lock.isHeldByCurrentThread() && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

}
