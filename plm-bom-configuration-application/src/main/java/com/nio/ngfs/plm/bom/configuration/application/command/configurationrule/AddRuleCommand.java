package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.application.service.ConfigurationRuleGroupApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleGroupDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.AddRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    private final ConfigurationRuleGroupApplicationService configurationRuleGroupApplicationService;

    @Override
    protected AddRuleRespDto executeCommand(AddRuleCmd cmd) {
        // 处理Group
        ConfigurationRuleGroupAggr ruleGroupAggr = ConfigurationRuleGroupFactory.create(cmd);
        // 新增Group
        ruleGroupAggr.add();
        // 校验Defined By
        configurationRuleGroupDomainService.checkDefinedBy(ruleGroupAggr);
        // 校验Driving Feature和Constrained Feature
        configurationRuleGroupApplicationService.checkDrivingAndConstrainedFeature(ruleGroupAggr);
        // 保存到数据库
        configurationRuleGroupRepository.save(ruleGroupAggr);
        return new AddRuleRespDto();
    }

}
