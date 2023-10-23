package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.ConfigurationRuleGroupApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleGroupDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.DeleteGroupCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.DeleteGroupRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 删除Group
 *
 * @author xiaozhou.tu
 * @date 2023/10/23
 */
@Component
@RequiredArgsConstructor
public class DeleteGroupCommand extends AbstractLockCommand<DeleteGroupCmd, DeleteGroupRespDto> {

    private final ConfigurationRuleRepository configurationRuleRepository;
    private final ConfigurationRuleGroupRepository configurationRuleGroupRepository;
    private final ConfigurationRuleGroupDomainService configurationRuleGroupDomainService;
    private final ConfigurationRuleGroupApplicationService configurationRuleGroupApplicationService;

    @Override
    protected String getLockKey(DeleteGroupCmd cmd) {
        return RedisKeyConstant.CONFIGURATION_RULE_GROUP_DELETE_LOCK + cmd.getGroupId();
    }

    @Override
    protected DeleteGroupRespDto executeWithLock(DeleteGroupCmd cmd) {
        ConfigurationRuleGroupAggr ruleGroupAggr = configurationRuleGroupDomainService.getAndCheckAggr(cmd.getGroupId());
        // 删除Group
        ruleGroupAggr.delete();
        // 查询Group下的Rule列表
        List<ConfigurationRuleAggr> ruleAggrList = configurationRuleRepository.queryByGroupId(ruleGroupAggr.getId());
        // 校验删除Group
        configurationRuleGroupApplicationService.checkDeleteGroup(ruleAggrList);
        // 删除Rule
        ruleAggrList.forEach(ConfigurationRuleAggr::delete);
        ((DeleteGroupCommand) AopContext.currentProxy()).deleteRuleAndGroup(ruleGroupAggr, ruleAggrList);
        return new DeleteGroupRespDto();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRuleAndGroup(ConfigurationRuleGroupAggr ruleGroupAggr, List<ConfigurationRuleAggr> ruleAggrList) {
        configurationRuleGroupRepository.remove(ruleGroupAggr);
        configurationRuleRepository.batchRemove(ruleAggrList);
    }

}
