package com.sh.beer.market.application.command;


import com.sh.beer.market.sdk.dto.common.Cmd;

/**
 * @author
 * @date 2023/7/13
 */
public abstract class AbstractCommand<C extends Cmd, Resp> implements Command<C, Resp> {

    @Override
    public Resp execute(C c) {
        try {
            validate(c);
            return executeCommand(c);
        } finally {
            close();
        }
    }

    /**
     * 校验
     *
     * @param c c
     */
    protected void validate(C c) {
    }

    /**
     * 执行命令
     *
     * @param c c
     * @return 结果
     */
    protected abstract Resp executeCommand(C c);

    /**
     * 关闭
     */
    protected void close() {
    }

}
