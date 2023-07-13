package com.nio.ngfs.plm.bom.configuration.application.command.oxo;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.EditGroupRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoSnapshotCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoSnapshootCommand extends AbstractLockCommand<OxoSnapshotCmd, Object> {


    @Override
    protected String getLockKey(OxoSnapshotCmd editGroupCmd) {
        return null;
    }

    @Override
    protected Object executeWithLock(OxoSnapshotCmd editGroupCmd) {
        return null;
    }

    public void saveSnapshot(OxoSnapshotCmd cmd) {



    }
}
