package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;

import com.google.common.collect.Lists;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRulePurposeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleStatusEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.RemoveRuleCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class RemoveRuleCommand extends AbstractLockCommand<RemoveRuleCmd, Boolean> {

    private final ConfigurationRuleRepository configurationRuleRepository;


    private final List<Integer> purposeLists = Lists.newArrayList(
            ConfigurationRulePurposeEnum.SALES_TO_ENG.getCode(),
            ConfigurationRulePurposeEnum.SALES_TO_SALES.getCode(),
            ConfigurationRulePurposeEnum.SALES_INCLUSIVE_SALES.getCode(),
            ConfigurationRulePurposeEnum.SALES_EXCLUSIVE_SALES.getCode());

    @Override
    protected String getLockKey(RemoveRuleCmd removeRuleCmd) {
        return RedisKeyConstant.CONFIGURATION_RULE_RULE_REMOVE_LOCK;
    }


    @Override
    protected Boolean executeWithLock(RemoveRuleCmd removeRuleCmd) {

        List<Long> ruleIds = removeRuleCmd.getRuleIds();
        String userName = removeRuleCmd.getUserName();
        List<ConfigurationRuleAggr> ruleAggrList = configurationRuleRepository.queryByRuleIdList(ruleIds, false);
        if (CollectionUtils.isEmpty(ruleAggrList)) {
            throw new BusinessException(ConfigErrorCode.RULE_ID_ERROR);
        }
        //校验数据是否不为remove
        ruleAggrList.forEach(ruleInfo -> {
            if (StringUtils.equals(ConfigurationRuleChangeTypeEnum.REMOVE.getChangeType(), ruleInfo.getChangeType())) {
                throw new BusinessException(MessageFormat.format(ConfigErrorCode.RULE_CHANGE_TYPE_ERROR.getMessage(), ruleInfo.getRuleNumber(), ruleInfo.getRuleVersion()));
            }
            if (!purposeLists.contains(ruleInfo.getPurpose())) {
                throw new BusinessException(MessageFormat.format(ConfigErrorCode.PURPOSE_ERROR.getMessage(), ruleInfo.getRuleNumber(), ruleInfo.getRuleVersion()));
            }
        });
        //根据rule_number批量查询
        List<ConfigurationRuleAggr> configurationRules =
                configurationRuleRepository.queryByRuleNumbers(ruleAggrList.stream().map(ConfigurationRuleAggr::getRuleNumber).distinct().toList());
        //Rule最新Rev条目 且 Status为Released（Change Type不为Remove）
        Map<String, List<ConfigurationRuleAggr>> configurationRuleByRuleNumberMap = configurationRules.stream().collect(Collectors.groupingBy(ConfigurationRuleAggr::getRuleNumber));
        configurationRuleByRuleNumberMap.forEach((ruleNumber, configurationRuleAggrs) -> {
            ConfigurationRuleAggr configurationRuleAggr =
                    configurationRuleAggrs.stream().sorted(Comparator.comparing(ConfigurationRuleAggr::getRuleVersion).reversed()).findFirst().get();
            if (StringUtils.equals(configurationRuleAggr.getStatus(), ConfigurationRuleStatusEnum.RELEASED.getStatus()) &&
                    !StringUtils.equals(configurationRuleAggr.getChangeType(), ConfigurationRuleChangeTypeEnum.REMOVE.getChangeType())) {
                throw new BusinessException(MessageFormat.format(ConfigErrorCode.RULE_CHANGE_TYPE_ERROR.getMessage(), ruleNumber, configurationRuleAggr.getRuleVersion()));
            }
        });
        /**
         * 由于是成对的Rule，因此在Remove其中一条版本的Rule时，
         * 系统需自动Remove另一条相应版本的Rule
         * 确保同时Remove成对Rule的相同版本条目
         */
        configurationRuleRepository.batchUpdate(ConfigurationRuleFactory.buildRemoveRuleAggr(ruleAggrList,userName));
        return true;
    }
}
