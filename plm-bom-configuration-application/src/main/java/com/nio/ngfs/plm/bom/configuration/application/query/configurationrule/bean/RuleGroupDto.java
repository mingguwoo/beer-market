package com.nio.ngfs.plm.bom.configuration.application.query.configurationrule.bean;

import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsConfigurationRuleEntity;
import lombok.Data;

import java.util.List;

/**
 * 同一个Rule Id的Rule分组
 *
 * @author xiaozhou.tu
 * @date 2023/10/31
 */
@Data
public class RuleGroupDto {

    private List<BomsConfigurationRuleEntity> ruleList;

}
