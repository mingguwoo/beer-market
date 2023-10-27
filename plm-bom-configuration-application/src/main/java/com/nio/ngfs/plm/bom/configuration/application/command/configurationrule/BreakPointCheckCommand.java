package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;


import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.SetBreakPointCmd;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class BreakPointCheckCommand extends AbstractCommand<SetBreakPointCmd, Boolean> {


    private final ConfigurationRuleRepository configurationRuleRepository;

    private final ConfigurationRuleDomainService configurationRuleDomainService;


    @Override
    protected Boolean executeCommand(SetBreakPointCmd setBreakPointCmd) {

        //查询 所有
        List<ConfigurationRuleAggr> configurationRuleAggrs =
                configurationRuleRepository.queryByRuleNumbers(Lists.newArrayList());

         configurationRuleDomainService.checkHardRule(configurationRuleAggrs);

        return null;
    }
}