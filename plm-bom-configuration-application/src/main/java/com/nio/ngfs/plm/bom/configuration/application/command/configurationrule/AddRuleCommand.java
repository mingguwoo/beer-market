package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.AddRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 新增Rule
 *
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Component
@RequiredArgsConstructor
public class AddRuleCommand extends AbstractLockCommand<AddRuleCmd, AddRuleRespDto> {

    @Override
    protected String getLockKey(AddRuleCmd cmd) {
        return null;
    }

    @Override
    protected AddRuleRespDto executeWithLock(AddRuleCmd cmd) {
        return null;
    }

}
