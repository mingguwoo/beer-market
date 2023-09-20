package com.nio.ngfs.plm.bom.configuration.application.command.feature.common;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.common.FeatureAggrThreadLocal;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;

/**
 * @author xiaozhou.tu
 * @date 2023/9/20
 */
public abstract class AbstractFeatureCommand<C extends Cmd, Resp> extends AbstractLockCommand<C, Resp> {

    @Override
    protected void close() {
        FeatureAggrThreadLocal.remove();
    }

}
