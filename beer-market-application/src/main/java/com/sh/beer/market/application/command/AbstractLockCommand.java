package com.sh.beer.market.application.command;

import com.sh.beer.market.common.lock.RedissonLocker;
import com.sh.beer.market.sdk.dto.common.Cmd;

import javax.annotation.Resource;

/**
 * 带redis分布式锁的Command，需要加锁控制并发的场景可使用
 *
 * @author
 * @date 2023/7/10
 */
public abstract class AbstractLockCommand<C extends Cmd, Resp> extends AbstractCommand<C, Resp> {

    @Resource
    private RedissonLocker redissonLocker;

    @Override
    protected Resp executeCommand(C c) {
        return redissonLocker.executeWithLock(getLockKey(c), getLockTime(c), () -> executeWithLock(c));
    }

    /**
     * 获取锁的key
     *
     * @param c c
     * @return key
     */
    protected abstract String getLockKey(C c);

    /**
     * 获取最大加锁等待时间（秒）
     *
     * @param c c
     * @return 加锁时间
     */
    protected Long getLockTime(C c) {
        return null;
    }

    /**
     * 执行加锁
     *
     * @param c c
     * @return 结果
     */
    protected abstract Resp executeWithLock(C c);

}
