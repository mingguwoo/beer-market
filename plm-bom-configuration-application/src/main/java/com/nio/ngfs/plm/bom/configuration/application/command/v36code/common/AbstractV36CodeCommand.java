package com.nio.ngfs.plm.bom.configuration.application.command.v36code.common;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.common.V36CodeLibraryAggrThreadLocal;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;

/**
 * @author xiaozhou.tu
 * @date 2023/9/20
 */
public abstract class AbstractV36CodeCommand<C extends Cmd, Resp> extends AbstractLockCommand<C, Resp> {

    @Override
    protected void close() {
        V36CodeLibraryAggrThreadLocal.remove();
    }

}
