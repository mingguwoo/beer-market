package com.nio.ngfs.plm.bom.configuration.application.command;

import com.nio.ngfs.plm.bom.configuration.common.lock.RedissonLocker;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;

import javax.annotation.Resource;

/**
 * @author xiaozhou.tu
 * @date 2023/7/10
 */
public abstract class AbstractLockCommand<C extends Cmd, Resp> implements Command<C, Resp> {

    @Resource
    private RedissonLocker redissonLocker;

    @Override
    public Resp execute(C c) {
        return redissonLocker.executeWithLock(getLockKey(c), () -> executeWithLock(c));
    }

    /**
     * 获取锁的key
     *
     * @param c c
     * @return key
     */
    protected abstract String getLockKey(C c);

    /**
     * 执行加锁
     *
     * @param c c
     * @return 结果
     */
    protected abstract Resp executeWithLock(C c);

}
