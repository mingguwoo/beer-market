package com.sh.beer.market.application.service.impl;


import com.sh.beer.market.application.service.ConfigurationRuleGroupApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author
 * @date 2023/10/17
 */
@Service
@RequiredArgsConstructor
public class ConfigurationRuleGroupApplicationServiceImpl implements ConfigurationRuleGroupApplicationService {

   /* @Override
    public void checkDeleteGroup(List<ConfigurationRuleAggr> ruleAggrList) {
        ruleAggrList.forEach(ruleAggr -> {
            if (!(Objects.equals(ruleAggr.getRuleVersion(), ConfigConstants.VERSION_AA) &&
                    ruleAggr.isStatus(ConfigurationRuleStatusEnum.IN_WORK))) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_GROUP_CAN_NOT_DELETE);
            }
        });
    }*/

}
