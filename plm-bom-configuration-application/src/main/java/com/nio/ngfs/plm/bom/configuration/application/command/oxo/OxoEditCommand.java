package com.nio.ngfs.plm.bom.configuration.application.command.oxo;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoEditInfoCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoEditCommand extends AbstractLockCommand<OxoEditInfoCmd, Object> {

    @Override
    protected String getLockKey(OxoEditInfoCmd cmd) {
        return null;
    }

    @Override
    protected Object executeWithLock(OxoEditInfoCmd cmd) {
        return null;
    }

    public void  edit(OxoEditInfoCmd cmd) {


    }



}
