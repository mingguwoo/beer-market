package com.nio.ngfs.plm.bom.configuration.application.command.oxo;


import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.EditGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.EditGroupRespDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request.OxoAddCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class OxoAddCommand extends AbstractLockCommand<OxoAddCmd, Object> {
    @Override
    protected String getLockKey(OxoAddCmd cmd) {
        return null;
    }

    @Override
    protected Object executeWithLock(OxoAddCmd cmd) {


        return new Object();
    }


    /**
     * 添加 optionCode FeatureCode至OXO
     * @param cmd
     */
    public void add(OxoAddCmd cmd) {









    }
}
