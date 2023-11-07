package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.ConfigurationRuleApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleGroupDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.AddRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 新增Rule
 *
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Component
@RequiredArgsConstructor
public class AddRuleCommand extends AbstractCommand<AddRuleCmd, AddRuleRespDto> {

    private final ConfigurationRuleGroupRepository configurationRuleGroupRepository;
    private final ConfigurationRuleRepository configurationRuleRepository;
    private final ConfigurationRuleGroupDomainService configurationRuleGroupDomainService;
    private final ConfigurationRuleDomainService configurationRuleDomainService;
    private final ConfigurationRuleApplicationService configurationRuleApplicationService;

    @Override
    protected AddRuleRespDto executeCommand(AddRuleCmd cmd) {
        ConfigurationRuleGroupAggr ruleGroupAggr = ConfigurationRuleGroupFactory.create(cmd);
        // 新增Group
        ruleGroupAggr.add();
        // 校验Defined By
        configurationRuleGroupDomainService.checkDefinedBy(ruleGroupAggr);
        // 创建Rule
        List<ConfigurationRuleAggr> ruleAggrList = configurationRuleDomainService.createNewRule(cmd);
        ruleAggrList.forEach(ConfigurationRuleAggr::add);
        // 校验Driving Feature和Constrained Feature
        configurationRuleApplicationService.checkDrivingAndConstrainedFeature(ruleGroupAggr, ruleAggrList);
        // 针对每一个Driving列，校验Constrained Feature下只能有一个Option为实心圆或-
        configurationRuleDomainService.checkOptionMatrixByConstrainedFeature(ruleAggrList);
        // 校验Rule Driving下的Constrained打点不重复
        configurationRuleDomainService.checkRuleDrivingConstrainedRepeat(ruleAggrList);
        // 处理双向Rule
        ruleAggrList = configurationRuleDomainService.handleBothWayRule(ruleAggrList);
        // Rule分配Rule Number
        configurationRuleDomainService.generateRuleNumber(ruleAggrList);
        // 保存到数据库
        ((AddRuleCommand) AopContext.currentProxy()).saveRuleAndGroup(ruleGroupAggr, ruleAggrList);
        return new AddRuleRespDto(ruleGroupAggr.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRuleAndGroup(ConfigurationRuleGroupAggr ruleGroupAggr, List<ConfigurationRuleAggr> ruleAggrList) {
        configurationRuleGroupRepository.save(ruleGroupAggr);
        if (CollectionUtils.isNotEmpty(ruleAggrList)) {
            ruleAggrList.forEach(ruleAggr -> ruleAggr.setGroupId(ruleGroupAggr.getId()));
            configurationRuleRepository.batchSave(ruleAggrList);
        }
    }

}
