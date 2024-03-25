package com.sh.beer.market.application.command.configurationrule;


import com.sh.beer.market.application.command.AbstractCommand;
import com.sh.beer.market.sdk.dto.configurationrule.request.AddRuleCmd;
import com.sh.beer.market.sdk.dto.configurationrule.response.AddRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
@RequiredArgsConstructor
public class BreakPointCheckCommand extends AbstractCommand<AddRuleCmd, AddRuleRespDto> {


    /*private final ConfigurationRuleRepository configurationRuleRepository;

    private final ConfigurationRuleGroupRepository configurationRuleGroupRepository;

    private final ConfigurationRuleDomainService configurationRuleDomainService;*/


    @Override
    protected AddRuleRespDto executeCommand(AddRuleCmd breakPointCheckCmd) {
        /*RuleViewCheckRespDto ruleViewCheckRespDto = new RuleViewCheckRespDto();
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
        return ruleViewCheckRespDto;*/
        return null;
    }
}