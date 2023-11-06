package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;


import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleStatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.BreakPointCheckCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.RuleViewCheckRespDto;
import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class BreakPointCheckCommand extends AbstractCommand<BreakPointCheckCmd, RuleViewCheckRespDto> {


    private final ConfigurationRuleRepository configurationRuleRepository;

    private final ConfigurationRuleGroupRepository configurationRuleGroupRepository;

    private final ConfigurationRuleDomainService configurationRuleDomainService;


    @Override
    protected RuleViewCheckRespDto executeCommand(BreakPointCheckCmd breakPointCheckCmd) {
        RuleViewCheckRespDto ruleViewCheckRespDto = new RuleViewCheckRespDto();
        String model = breakPointCheckCmd.getModel();
        String modelCode = breakPointCheckCmd.getModel() + " " + breakPointCheckCmd.getModelYear();

        List<ConfigurationRuleGroupAggr> configurationRuleGroupAggrs =
                configurationRuleGroupRepository.queryConfigurationRuleGroupsByDefinedBy(Lists.newArrayList(model, modelCode));
        if (CollectionUtils.isEmpty(configurationRuleGroupAggrs)) {
            return ruleViewCheckRespDto;
        }
        List<Long> groupIds = configurationRuleGroupAggrs.stream().map(ConfigurationRuleGroupAggr::getId).distinct().toList();
        //对Status为Released的Rule条目进行Eff-in和Eff-out设置合理性检查
        List<ConfigurationRuleAggr> configurationRuleAggrs =
                configurationRuleRepository.queryByIdsAndStatus(groupIds, ConfigurationRuleStatusEnum.RELEASED.getStatus(), true);

        if (CollectionUtils.isEmpty(configurationRuleAggrs)) {
            return ruleViewCheckRespDto;
        }
        //硬校验
        ruleViewCheckRespDto.setRedRuleIds(configurationRuleDomainService.checkHardRule(configurationRuleAggrs));
        //软校验
        ruleViewCheckRespDto.setYellowRuleIds(configurationRuleDomainService.checkSoftRule(configurationRuleAggrs));
        return ruleViewCheckRespDto;
    }
}