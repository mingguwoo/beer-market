package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;


import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.SetBreakPointCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SetBreakPointCommand extends AbstractLockCommand<SetBreakPointCmd, Boolean> {

    private final ConfigurationRuleRepository configurationRuleRepository;


    @Override
    protected String getLockKey(SetBreakPointCmd setBreakPointCmd) {
        return null;
    }

    @Override
    protected Boolean executeWithLock(SetBreakPointCmd setBreakPointCmd) {

        List<Long> ruleIds = setBreakPointCmd.getRuleIds();

        List<ConfigurationRuleAggr> ruleAggrList =
                configurationRuleRepository.queryByRuleIdList(ruleIds, false);

        //ruleAggrList.
        return null;
    }
}
