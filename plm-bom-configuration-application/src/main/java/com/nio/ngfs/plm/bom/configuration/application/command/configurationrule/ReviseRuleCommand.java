package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;

import com.nio.bom.share.constants.CommonConstants;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.ReviseRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.ReviseRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

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
        String newVersion = configurationRuleDomainService.getReviseVersion(originalAggr.getRuleVersion());
            //此时id先不置为null，防止后续找不到双向rule
        originalAggr.revise(newVersion,cmd.getReviser());
        //考虑双向rule
        if (Objects.equals(originalAggr.getPurpose(), CommonConstants.INT_THREE) || Objects.equals(originalAggr.getPurpose(),CommonConstants.INT_FOUR)){
            ConfigurationRuleAggr originalDoubleAggr = configurationRuleDomainService.findAnotherBothWayRule(originalAggr);
            //id置为null
            originalAggr.setId(null);
            originalDoubleAggr.setId(null);
            originalDoubleAggr.revise(newVersion,cmd.getReviser());
            //重新分配rulePairId
            originalAggr.resetRulePairId();
            originalDoubleAggr.resetRulePairId(originalAggr.getRulePairId());
            configurationRuleRepository.batchSave(Arrays.asList(originalAggr,originalDoubleAggr));
        }
        else{
            //id置为null
            originalAggr.setId(null);
            configurationRuleRepository.save(originalAggr);
        }
        return new ReviseRuleRespDto();
    }

}
