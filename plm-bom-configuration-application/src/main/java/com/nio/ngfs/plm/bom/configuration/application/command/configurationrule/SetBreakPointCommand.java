package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;


import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.SetBreakPointCmd;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SetBreakPointCommand extends AbstractLockCommand<SetBreakPointCmd, Boolean> {

    private final ConfigurationRuleRepository configurationRuleRepository;


    @Override
    protected String getLockKey(SetBreakPointCmd setBreakPointCmd) {
        return null;
    }

    @Override
    protected Boolean executeWithLock(SetBreakPointCmd setBreakPointCmd) {

        List<Long> ruleIds = setBreakPointCmd.getRuleIds();
        Date effIn = setBreakPointCmd.getEffIn();
        Date effOut = setBreakPointCmd.getEffOut();
        if (Objects.nonNull(effIn) && Objects.nonNull(effOut) && effIn.compareTo(effOut) > 0) {
            throw new BusinessException(ConfigErrorCode.EFF_OUT_TIME_ERROR);
        }
        List<ConfigurationRuleAggr> ruleAggrList =
                configurationRuleRepository.queryByRuleIdList(ruleIds, false);

        if (CollectionUtils.isEmpty(ruleAggrList)) {
            throw new BusinessException(ConfigErrorCode.RULE_ID_ERROR);
        }

        ruleAggrList.forEach(ruleAggr->{

            if (Objects.nonNull(effIn)){

            }
        });





        return null;
    }
}
