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
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleGroupDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.EditGroupAndRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.EditGroupAndRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final ConfigurationRuleGroupRepository configurationRuleGroupRepository;
    private final ConfigurationRuleGroupDomainService configurationRuleGroupDomainService;
    private final ConfigurationRuleApplicationService configurationRuleApplicationService;

    @Override
    protected String getLockKey(EditGroupAndRuleCmd cmd) {
        return RedisKeyConstant.CONFIGURATION_RULE_GROUP_LOCK + cmd.getGroupId();
    }

    @Override
    protected EditGroupAndRuleRespDto executeWithLock(EditGroupAndRuleCmd cmd) {
        // 获取Group聚合根
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
        if (CollectionUtils.isNotEmpty(context.getErrorMessageList())) {
            return new EditGroupAndRuleRespDto(context.getErrorMessageList());
        }
        // 保存到数据库
        ((EditGroupAndRuleCommand) AopContext.currentProxy()).saveRuleAndGroup(ruleGroupAggr, context);
        return new EditGroupAndRuleRespDto();
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRuleAndGroup(ConfigurationRuleGroupAggr ruleGroupAggr, EditConfigurationRuleContext context) {
        configurationRuleGroupRepository.save(ruleGroupAggr);
        // 新增或更新Rule
        List<ConfigurationRuleAggr> addOrUpdateRuleAggrList = context.getAddOrUpdateRuleList();
        addOrUpdateRuleAggrList.forEach(ruleAggr -> {
            if (ruleAggr.getGroupId() == null) {
                ruleAggr.setGroupId(ruleGroupAggr.getId());
            }
        });
        configurationRuleRepository.batchSave(addOrUpdateRuleAggrList);
        // 删除Rule
        configurationRuleRepository.batchRemove(context.getDeleteRuleList());
    }

}
