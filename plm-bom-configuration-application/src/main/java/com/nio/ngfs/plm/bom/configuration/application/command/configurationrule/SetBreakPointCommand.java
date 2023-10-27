package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;


import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.SetBreakPointCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class SetBreakPointCommand extends AbstractLockCommand<SetBreakPointCmd, Boolean> {

    private final ConfigurationRuleRepository configurationRuleRepository;

    private final ConfigurationRuleDomainService configurationRuleDomainService;


    @Override
    protected String getLockKey(SetBreakPointCmd setBreakPointCmd) {
        return null;
    }


    @Override
    protected Boolean executeWithLock(SetBreakPointCmd setBreakPointCmd) {

        List<Long> ruleIds = setBreakPointCmd.getRuleIds();
        Date effIn = setBreakPointCmd.getEffIn();
        Date effOut = setBreakPointCmd.getEffOut();
        //校验时间
        if (Objects.nonNull(effIn) && Objects.nonNull(effOut) && effIn.compareTo(effOut) > 0) {
            throw new BusinessException(ConfigErrorCode.EFF_OUT_TIME_ERROR);
        }
        List<ConfigurationRuleAggr> ruleAggrList =
                configurationRuleRepository.queryByRuleIdList(ruleIds, false);
        if (CollectionUtils.isEmpty(ruleAggrList)) {
            throw new BusinessException(ConfigErrorCode.RULE_ID_ERROR);
        }
        ruleAggrList.forEach(ruleAggr -> {
            //所选日期是否早于/等于Rule条目的Eff-out值
            if (Objects.nonNull(effIn) && effIn.compareTo(ruleAggr.getEffOut()) > 0) {
                throw new BusinessException(MessageFormat.format(ConfigErrorCode.EFF_OUT_TIME_ERROR.getMessage(), ruleAggr.getRuleNumber(), ruleAggr.getRuleVersion()));
            }
            if (Objects.nonNull(effOut) && ruleAggr.getEffIn().compareTo(effOut) > 0) {
                throw new BusinessException(MessageFormat.format(ConfigErrorCode.EFF_IN_TIME_ERROR.getMessage(), ruleAggr.getRuleNumber(), ruleAggr.getRuleVersion()));
            }
        });

        if (Objects.nonNull(effOut)) {
            configurationRuleDomainService.checkNextRevConfigurationRule(ruleAggrList.stream().filter(x -> StringUtils.equalsAny(x.getChangeType()
                    , ConfigurationRuleChangeTypeEnum.ADD.getChangeType(), ConfigurationRuleChangeTypeEnum.MODIFY.getChangeType())).toList());
        }

        //更新eff_in eff_out
        configurationRuleDomainService.updateEffInOrEffOut(ruleIds, ConfigurationRuleFactory.createUpdateInfo(setBreakPointCmd));

        return true;
    }


}
