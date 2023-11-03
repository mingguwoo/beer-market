package com.nio.ngfs.plm.bom.configuration.application.command.configurationrule;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.ConfigurationRuleRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.configurationrule.ConfigurationRuleDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.ReleaseRuleCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.response.ReleaseRuleRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 发布Rule
 *
 * @author xiaozhou.tu
 * @date 2023/10/23
 */
@Component
@RequiredArgsConstructor
public class ReleaseRuleCommand extends AbstractLockCommand<ReleaseRuleCmd, ReleaseRuleRespDto> {

    private final ConfigurationRuleRepository configurationRuleRepository;
    private final ConfigurationRuleDomainService configurationRuleDomainService;

    @Override
    protected String getLockKey(ReleaseRuleCmd cmd) {
        return RedisKeyConstant.CONFIGURATION_RULE_RULE_RELEASE_LOCK;
    }

    @Override
    protected ReleaseRuleRespDto executeWithLock(ReleaseRuleCmd cmd) {
        // 查找Rule聚合根列表
        List<ConfigurationRuleAggr> ruleAggrList = configurationRuleRepository.queryByRuleIdList(cmd.getRuleIdList(), false);
        // 筛选可以Release的Rule
        ruleAggrList = ruleAggrList.stream().filter(ConfigurationRuleAggr::canRelease).collect(Collectors.toList());
        // 处理双向Rule Release
        configurationRuleDomainService.releaseBothWayRule(ruleAggrList);
        // 发布Rule
        ruleAggrList = ruleAggrList.stream().filter(ConfigurationRuleAggr::release).toList();
        // 保存到数据库
        configurationRuleRepository.batchSave(ruleAggrList);
        return new ReleaseRuleRespDto();
    }

}
