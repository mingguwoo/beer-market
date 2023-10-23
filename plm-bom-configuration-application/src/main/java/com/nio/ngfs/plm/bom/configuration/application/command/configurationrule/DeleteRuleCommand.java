package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.DeleteRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.DeleteRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 删除Rule
 *
 * @author xiaozhou.tu
 * @date 2023/10/23
 */
@Component
@RequiredArgsConstructor
public class DeleteRuleCommand extends AbstractLockCommand<DeleteRuleCmd, DeleteRuleRespDto> {

    private final ConfigurationRuleDomainService configurationRuleDomainService;
    private final ConfigurationRuleRepository configurationRuleRepository;

    @Override
    protected String getLockKey(DeleteRuleCmd cmd) {
        return RedisKeyConstant.CONFIGURATION_RULE_RULE_DELETE_LOCK + cmd.getRuleId();
    }

    @Override
    protected DeleteRuleRespDto executeWithLock(DeleteRuleCmd cmd) {
        ConfigurationRuleAggr ruleAggr = configurationRuleDomainService.getAndCheckAggr(cmd.getRuleId());
        // 删除Rule
        ruleAggr.delete();
        if (ruleAggr.getRulePurposeEnum().isBothWay()) {
            ConfigurationRuleAggr anotherRuleAggr = configurationRuleDomainService.findAnotherBothWayRule(ruleAggr);
            anotherRuleAggr.delete();
            configurationRuleRepository.batchRemove(Lists.newArrayList(ruleAggr, anotherRuleAggr));
        } else {
            configurationRuleRepository.batchRemove(Lists.newArrayList(ruleAggr));
        }
        return new DeleteRuleRespDto();
    }

}
