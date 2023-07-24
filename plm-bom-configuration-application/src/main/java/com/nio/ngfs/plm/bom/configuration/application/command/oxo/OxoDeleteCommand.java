package com.nio.ngfs.plm.bom.configuration.application.command.oxo;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoDeleteCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoDeleteCommand extends AbstractLockCommand<OxoDeleteCmd, Boolean> {


    @Override
    protected String getLockKey(OxoDeleteCmd cmd) {
        return null;
    }

    @Override
    protected Boolean executeWithLock(OxoDeleteCmd cmd) {




        return true;
    }




    public Boolean delete(OxoDeleteCmd cmd) {





        return true;
    }
}
