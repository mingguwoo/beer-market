package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.ConfigurationRuleApplicationService;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.RuleOptionMatrixValueEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrulegroup.ConfigurationRuleGroupAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Service
@RequiredArgsConstructor
public class ConfigurationRuleApplicationServiceImpl implements ConfigurationRuleApplicationService {

    @Override
    public List<ConfigurationRuleAggr> createNewRule(ConfigurationRuleGroupAggr ruleGroupAggr, AddRuleCmd cmd) {
        // 筛选有效的打点
        List<AddRuleCmd.RuleOptionDto> ruleOptionList = LambdaUtil.map(cmd.getRuleOptionList(), i -> !Objects.equals(i.getMatrixValue(),
                RuleOptionMatrixValueEnum.UNAVAILABLE.getCode()), Function.identity());
        if (CollectionUtils.isEmpty(ruleOptionList)) {
            return Lists.newArrayList();
        }
        // 按drivingOptionCode分组，生成Rule聚合根
        return LambdaUtil.groupBy(ruleOptionList, AddRuleCmd.RuleOptionDto::getDrivingOptionCode)
                .values().stream().map(ruleOptionDtoList -> ConfigurationRuleFactory.create(cmd, ruleOptionDtoList)).toList();
    }

}
