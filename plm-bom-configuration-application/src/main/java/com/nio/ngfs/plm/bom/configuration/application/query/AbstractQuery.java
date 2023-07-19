package com.nio.ngfs.plm.bom.configuration.application.query;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;

/**
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
public abstract class AbstractQuery<C extends Qry, Resp> implements Query<C, Resp> {

    @Override
    public Resp execute(C c) {
        validate(c);
        return executeQuery(c);
    }

    /**
     * 校验
     *
     * @param c c
     */
    protected void validate(C c) {
    }

    /**
     * 执行查询
     *
     * @param c c
     * @return 结果
     */
    protected abstract Resp executeQuery(C c);

}
