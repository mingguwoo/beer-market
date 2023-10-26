package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.ConfigurationRuleApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.context.EditConfigurationRuleContext;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleGroupDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.EditGroupAndRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.EditGroupAndRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 编辑Group和Rule
 *
 * @author xiaozhou.tu
 * @date 2023/10/25
 */
@Component
@RequiredArgsConstructor
public class EditGroupAndRuleCommand extends AbstractLockCommand<EditGroupAndRuleCmd, EditGroupAndRuleRespDto> {

    private final ConfigurationRuleRepository configurationRuleRepository;
    private final ConfigurationRuleGroupDomainService configurationRuleGroupDomainService;
    private final ConfigurationRuleApplicationService configurationRuleApplicationService;

    @Override
    protected String getLockKey(EditGroupAndRuleCmd cmd) {
        return RedisKeyConstant.CONFIGURATION_RULE_GROUP_LOCK + cmd.getGroupId();
    }

    @Override
    protected EditGroupAndRuleRespDto executeWithLock(EditGroupAndRuleCmd cmd) {
        ConfigurationRuleGroupAggr ruleGroupAggr = configurationRuleGroupDomainService.getAndCheckAggr(cmd.getGroupId());
        // 编辑Group
        ruleGroupAggr.edit(cmd);
        // 查找Rule聚合根列表
        List<ConfigurationRuleAggr> ruleAggrList = configurationRuleRepository.queryByGroupId(cmd.getGroupId());
        // 打点列表
        List<ConfigurationRuleOptionDo> ruleOptionDoList = LambdaUtil.map(cmd.getRuleOptionList(), i -> ConfigurationRuleFactory.create(i, cmd.getUpdateUser()));
        // 构建编辑Rule上下文
        EditConfigurationRuleContext context = configurationRuleApplicationService.buildEditConfigurationRuleContext(ruleGroupAggr, ruleAggrList, ruleOptionDoList);
        // 编辑Rule预处理
        configurationRuleApplicationService.preHandleEditRule(context);
        // 校验并处理Rule编辑
        configurationRuleApplicationService.checkAndProcessEditRule(context);
        return new EditGroupAndRuleRespDto();
    }

}
