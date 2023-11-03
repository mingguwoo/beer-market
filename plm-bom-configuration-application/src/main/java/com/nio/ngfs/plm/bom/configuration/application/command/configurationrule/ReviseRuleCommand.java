package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.ReviseRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.ReviseRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author bill.wang
 * @date 2023/10/24
 */
@Component
@RequiredArgsConstructor
public class ReviseRuleCommand extends AbstractCommand<ReviseRuleCmd, ReviseRuleRespDto> {

    private final ConfigurationRuleDomainService configurationRuleDomainService;
    private final ConfigurationRuleRepository configurationRuleRepository;

    @Override
    protected ReviseRuleRespDto executeCommand(ReviseRuleCmd cmd) {

        //创建聚合根并升版
        ConfigurationRuleAggr originalAggr = configurationRuleDomainService.getAndCheckAggr(cmd.getRuleId());
        ConfigurationRuleAggr newAggr = originalAggr.revise(cmd.getReviser());
        //考虑双向rule
        if (originalAggr.isBothWayRule()){
            ConfigurationRuleAggr originalDoubleAggr = configurationRuleDomainService.findAnotherBothWayRule(originalAggr);
            ConfigurationRuleAggr newDoubleAggr = originalDoubleAggr.revise(cmd.getReviser());
            //重新分配rulePairId
            newAggr.resetRulePairId();
            newDoubleAggr.resetRulePairId(newAggr.getRulePairId());
            configurationRuleRepository.batchSave(Arrays.asList(newAggr,newDoubleAggr));
        }
        else{
            configurationRuleRepository.save(newAggr);
        }
        return new ReviseRuleRespDto();
    }

}
