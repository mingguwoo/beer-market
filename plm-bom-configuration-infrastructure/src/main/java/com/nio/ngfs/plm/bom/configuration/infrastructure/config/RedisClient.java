package com.nio.ngfs.plm.bom.configuration.infrastructure.config;

import com.nio.bom.share.constants.CommonConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * Redis内存数据库客户端操作封装
 * @author luke.zhu
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RedisClient {

    private static final String LOCK_TITLE = "CONFIG_REDIS_LOCK:";
    private final StringRedisTemplate redisTemplate;
    private final RedissonClient redisClient;

    /**
     * 加锁
     *
     * @param lockName
     * @return
     */
    public boolean acquireLock(String lockName, long timeoutSeconds) {
        //声明key对象
        String key = LOCK_TITLE + lockName;
        //获取锁对象
        RLock lock = redisClient.getLock(key);

        try {
            //加锁，并且设置锁等待时间3秒
            return lock.tryLock(5, timeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("try lock failed", e);
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param key
     * @return
     */
    public void releaseLock(String key) {
        try {
            //获取锁对象
            RLock lock = redisClient.getLock(key);
            if (Objects.nonNull(lock)) {
                lock.unlock();
            }
        }catch (Exception e){
            log.info("releaseLock failed,key:{},error:{}",key, ExceptionUtils.getStackTrace(e));
        }
    }


    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置，不包含超期时间
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置，包含超期时间
     *
     * @param key
     * @param value
     * @param expireTime
     */
    public void expireSet(String key, String value, int expireTime) {
        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 自增长
     *
     * @param key
     * @return
     */
    public long incrBy(String key) {
        return redisTemplate.opsForValue().increment(key, CommonConstants.INT_ONE);
    }

}
