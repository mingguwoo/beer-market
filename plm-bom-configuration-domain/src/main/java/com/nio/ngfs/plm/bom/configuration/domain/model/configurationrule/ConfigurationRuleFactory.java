package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.common.utils.BeanConvertUtils;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.AddRuleCmd;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
public class ConfigurationRuleFactory {

    public static ConfigurationRuleAggr create(AddRuleCmd cmd, List<AddRuleCmd.RuleOptionDto> ruleOptionDtoList) {
        ConfigurationRuleAggr ruleAggr = ConfigurationRuleAggr.builder()
                .purpose(cmd.getPurpose())
                .createUser(cmd.getCreateUser())
                .updateUser(cmd.getCreateUser())
                .build();
        ruleAggr.setOptionList(LambdaUtil.map(ruleOptionDtoList, option -> create(option, cmd.getCreateUser(), ruleAggr)));
        return ruleAggr;
    }

    private static ConfigurationRuleOptionDo create(AddRuleCmd.RuleOptionDto ruleOptionDto, String createUser, ConfigurationRuleAggr ruleAggr) {
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


    public static List<ConfigurationRuleAggr> buildRemoveRuleAggr(List<ConfigurationRuleAggr> ruleAggrList,String userName){

        List<ConfigurationRuleAggr> configurationRuleAggrs= Lists.newArrayList();

        Date updateTime = new Date();
        ruleAggrList.forEach(rule->{
            ConfigurationRuleAggr ruleAggr=new ConfigurationRuleAggr();
            ruleAggr.setId(ruleAggr.getId());
            ruleAggr.setCreateUser(userName);
            ruleAggr.setUpdateTime(updateTime);
            configurationRuleAggrs.add(ruleAggr);

            if(Objects.nonNull(rule.getRulePairId()) && rule.getRulePairId()>0){
                ConfigurationRuleAggr copyRuleAggr = BeanConvertUtils.convertTo(ruleAggr,ConfigurationRuleAggr::new);
                copyRuleAggr.setId(rule.getRulePairId());
                configurationRuleAggrs.add(copyRuleAggr);
            }
        });
        return configurationRuleAggrs;
    }

}
