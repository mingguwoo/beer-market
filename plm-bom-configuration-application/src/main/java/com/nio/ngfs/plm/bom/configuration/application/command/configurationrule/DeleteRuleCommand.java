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
        return RedisKeyConstant.CONFIGURATION_RULE_GROUP_LOCK + cmd.getGroupId();
    }

    @Override
    protected DeleteRuleRespDto executeWithLock(DeleteRuleCmd cmd) {
        // 查询Rule聚合根
        ConfigurationRuleAggr ruleAggr = configurationRuleDomainService.getAndCheckAggr(cmd.getRuleId());
        // Rule删除
        ruleAggr.delete();
        if (ruleAggr.isBothWayRule()) {
            // 查找双向Rule
            ConfigurationRuleAggr anotherRuleAggr = configurationRuleDomainService.findAnotherBothWayRule(ruleAggr);
            // 双向Rule删除
            anotherRuleAggr.delete();
            // 保存到数据库
            configurationRuleRepository.batchRemove(Lists.newArrayList(ruleAggr, anotherRuleAggr));
        } else {
            // 保存到数据库
            configurationRuleRepository.batchRemove(Lists.newArrayList(ruleAggr));
        }
        return new DeleteRuleRespDto();
    }

}
