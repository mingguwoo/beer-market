package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRulePurposeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.RuleOptionDto;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public class ConfigurationRuleFactory {

    public static ConfigurationRuleAggr createWithOptionList(Integer purpose, String createUser, List<ConfigurationRuleOptionDo> optionList) {
        ConfigurationRuleAggr ruleAggr = ConfigurationRuleAggr.builder()
                .purpose(purpose)
                .createUser(createUser)
                .updateUser(createUser)
                .build();
        ruleAggr.setOptionList(optionList);
        optionList.forEach(option -> option.setRule(ruleAggr));
        return ruleAggr;
    }

    public static ConfigurationRuleAggr create(Integer purpose, String createUser, List<RuleOptionDto> ruleOptionDtoList) {
        ConfigurationRuleAggr ruleAggr = ConfigurationRuleAggr.builder()
                .purpose(purpose)
                .createUser(createUser)
                .updateUser(createUser)
                .build();
        ruleAggr.setOptionList(LambdaUtil.map(ruleOptionDtoList, option -> create(option, createUser, ruleAggr)));
        return ruleAggr;
    }

    private static ConfigurationRuleOptionDo create(RuleOptionDto ruleOptionDto, String createUser, ConfigurationRuleAggr ruleAggr) {
        return ConfigurationRuleOptionDo.builder()
                .rule(ruleAggr)
                .drivingOptionCode(ruleOptionDto.getDrivingOptionCode())
                .drivingFeatureCode(ruleOptionDto.getDrivingFeatureCode())
                .constrainedOptionCode(ruleOptionDto.getConstrainedOptionCode())
                .constrainedFeatureCode(ruleOptionDto.getConstrainedFeatureCode())
                .matrixValue(ruleOptionDto.getMatrixValue())
                .createUser(createUser)
                .updateUser(createUser)
                .build();
    }

    public static ConfigurationRuleOptionDo create(RuleOptionDto ruleOptionDto, String updateUser) {
        return ConfigurationRuleOptionDo.builder()
                .drivingOptionCode(ruleOptionDto.getDrivingOptionCode())
                .drivingFeatureCode(ruleOptionDto.getDrivingFeatureCode())
                .constrainedOptionCode(ruleOptionDto.getConstrainedOptionCode())
                .constrainedFeatureCode(ruleOptionDto.getConstrainedFeatureCode())
                .matrixValue(ruleOptionDto.getMatrixValue())
                .createUser(updateUser)
                .updateUser(updateUser)
                .build();
    }


    /**
     * @param ruleAggrList       根据ruleId查询的
     * @param configurationRules 根据ruleId查询的
     * @param userName
     * @return
     */
    public static List<ConfigurationRuleAggr> buildRemoveRuleAggr(List<ConfigurationRuleAggr> ruleAggrList,
                                                                  List<ConfigurationRuleAggr> configurationRules,
                                                                  String userName) {
        List<ConfigurationRuleAggr> configurationRuleAggrs = Lists.newArrayList();
        Date updateTime = new Date();
        ruleAggrList.forEach(rule -> {
            ConfigurationRuleAggr ruleAggr = new ConfigurationRuleAggr();
            ruleAggr.setId(ruleAggr.getId());
            ruleAggr.setCreateUser(userName);
            ruleAggr.setUpdateTime(updateTime);
            configurationRuleAggrs.add(ruleAggr);
            if (ConfigurationRulePurposeEnum.SALES_INCLUSIVE_SALES.getCode().equals(rule.getPurpose()) ||
                    ConfigurationRulePurposeEnum.SALES_EXCLUSIVE_SALES.getCode().equals(rule.getPurpose())) {
                List<Long> ids = configurationRules.stream().filter(x -> StringUtils.equals(rule.getRuleNumber(), x.getRuleNumber()) &&
                        StringUtils.equals(rule.getRuleVersion(), x.getRuleVersion()) && !Objects.equals(x.getId(), rule.getId())
                        && !Objects.equals(x.getPurpose(), rule.getPurpose())).map(ConfigurationRuleAggr::getId).distinct().toList();
                ids.forEach(id -> {
                    ConfigurationRuleAggr copyRuleAggr = BeanConvertUtils.convertTo(ruleAggr, ConfigurationRuleAggr::new);
                    copyRuleAggr.setId(id);
                    configurationRuleAggrs.add(copyRuleAggr);
                });
            }
        });
        return configurationRuleAggrs;
    }

}
