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
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
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

    private final ConfigurationRuleDomainService configurationRuleDomainService;


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
        // remove校验
        configurationRuleDomainService.checkConfigurationRuleRemove(ruleAggrList);
        /**
         * 由于是成对的Rule，因此在Remove其中一条版本的Rule时，
         * 系统需自动Remove另一条相应版本的Rule
         * 确保同时Remove成对Rule的相同版本条目
         */
        List<ConfigurationRuleAggr> anotherBothWayRuleAggrList = configurationRuleDomainService.batchFindAnotherBothWayRule(ruleAggrList);

        //更新
        configurationRuleRepository.batchUpdate(ConfigurationRuleFactory.buildRemoveRuleAggr(ruleAggrList,anotherBothWayRuleAggrList,userName));
        return true;
    }
}
