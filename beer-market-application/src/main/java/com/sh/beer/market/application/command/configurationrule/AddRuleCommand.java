package com.sh.beer.market.application.command.configurationrule;


import com.sh.beer.market.application.command.AbstractCommand;
import com.sh.beer.market.domain.model.configurationrule.ConfigurationRuleAggr;
import com.sh.beer.market.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.sh.beer.market.sdk.dto.configurationrule.request.AddRuleCmd;
import com.sh.beer.market.sdk.dto.configurationrule.response.AddRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 新增Rule
 *
 * @author
 * @date 2023/10/17
 */
@Component
@RequiredArgsConstructor
public class AddRuleCommand extends AbstractCommand<AddRuleCmd, AddRuleRespDto> {

    /*private final ConfigurationRuleGroupRepository configurationRuleGroupRepository;
    private final ConfigurationRuleRepository configurationRuleRepository;
    private final ConfigurationRuleGroupDomainService configurationRuleGroupDomainService;
    private final ConfigurationRuleDomainService configurationRuleDomainService;
    private final ConfigurationRuleApplicationService configurationRuleApplicationService;*/

    @Override
    protected AddRuleRespDto executeCommand(AddRuleCmd cmd) {
        /*ConfigurationRuleGroupAggr ruleGroupAggr = ConfigurationRuleGroupFactory.create(cmd);
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
        configurationRuleDomainService.checkRuleDrivingConstrainedRepeat(ruleAggrList, Lists.newArrayList());
        // 处理双向Rule
        ruleAggrList = configurationRuleDomainService.handleBothWayRule(ruleAggrList);
        // Rule分配Rule Number
        configurationRuleDomainService.generateRuleNumber(ruleAggrList);
        // 保存到数据库
        ((AddRuleCommand) AopContext.currentProxy()).saveRuleAndGroup(ruleGroupAggr, ruleAggrList);
        return new AddRuleRespDto(ruleGroupAggr.getId());*/
        return null;
    }

//    @Transactional(rollbackFor = Exception.class)
    public void saveRuleAndGroup(ConfigurationRuleGroupAggr ruleGroupAggr, List<ConfigurationRuleAggr> ruleAggrList) {
        /*configurationRuleGroupRepository.save(ruleGroupAggr);
        if (CollectionUtils.isNotEmpty(ruleAggrList)) {
            ruleAggrList.forEach(ruleAggr -> ruleAggr.setGroupId(ruleGroupAggr.getId()));
            configurationRuleRepository.batchSave(ruleAggrList);
        }*/
    }

}
