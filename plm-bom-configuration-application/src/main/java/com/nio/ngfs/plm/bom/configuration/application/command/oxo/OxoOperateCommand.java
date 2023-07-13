package com.nio.ngfs.plm.bom.configuration.application.command.oxo;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.EditGroupRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoOperateCommand extends AbstractLockCommand<EditGroupCmd, EditGroupRespDto> {


    @Override
    protected String getLockKey(EditGroupCmd editGroupCmd) {
        return null;
    }

    @Override
    protected EditGroupRespDto executeWithLock(EditGroupCmd editGroupCmd) {
        return null;
    }
}
