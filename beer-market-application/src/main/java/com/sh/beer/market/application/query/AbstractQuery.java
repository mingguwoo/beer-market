package com.sh.beer.market.application.query;


import com.sh.beer.market.sdk.dto.common.Qry;

/**
 * @author
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
