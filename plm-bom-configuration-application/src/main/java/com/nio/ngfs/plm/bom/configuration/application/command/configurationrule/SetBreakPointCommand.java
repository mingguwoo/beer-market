package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;


import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractCommand;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleFactory;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.SetBreakPointCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author wangchao.wang
 */
@Component
@RequiredArgsConstructor
public class SetBreakPointCommand extends AbstractCommand<SetBreakPointCmd, Boolean> {

    private final ConfigurationRuleDomainService configurationRuleDomainService;

    @Override
    protected Boolean executeCommand(SetBreakPointCmd setBreakPointCmd) {
        List<Long> ruleIds = setBreakPointCmd.getRuleIds();
        Date effIn = setBreakPointCmd.getEffIn();
        Date effOut = setBreakPointCmd.getEffOut();
        //校验时间
        if (Objects.nonNull(effIn) && Objects.nonNull(effOut) && effIn.compareTo(effOut) > 0) {
            throw new BusinessException(ConfigErrorCode.EFF_OUT_TIME_ERROR);
        }
        configurationRuleDomainService.checkNextRevConfigurationRule(ruleIds, effIn, effOut);
        //更新eff_in eff_out
        configurationRuleDomainService.updateEffInOrEffOut(ruleIds, ConfigurationRuleFactory.createUpdateInfo(setBreakPointCmd));
        return true;
    }


}
