package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.service.ConfigurationRuleGroupApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Service
@RequiredArgsConstructor
public class ConfigurationRuleGroupApplicationServiceImpl implements ConfigurationRuleGroupApplicationService {

    @Override
    public void checkDeleteGroup(List<ConfigurationRuleAggr> ruleAggrList) {
        ruleAggrList.forEach(ruleAggr -> {
            if (!(Objects.equals(ruleAggr.getRuleVersion(), ConfigConstants.VERSION_AA) &&
                    ruleAggr.isStatus(ConfigurationRuleStatusEnum.IN_WORK))) {
                throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_RULE_GROUP_CAN_NOT_DELETE);
            }
        });
    }

}
