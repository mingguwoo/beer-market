package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.DateUtils;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.domainobject.ConfigurationRuleOptionDo;
import com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums.ConfigurationRuleChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.RuleOptionDto;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request.SetBreakPointCmd;

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
     * @param ruleAggrList               根据ruleId查询的
     * @param anotherBothWayRuleAggrList 双向ruleId
     * @param userName
     * @return
     */
    public static List<ConfigurationRuleAggr> buildRemoveRuleAggr(List<ConfigurationRuleAggr> ruleAggrList,
                                                                  List<ConfigurationRuleAggr> anotherBothWayRuleAggrList,
                                                                  String userName) {

        List<Long> ids = Lists.newArrayList();
        ids.addAll(ruleAggrList.stream().map(ConfigurationRuleAggr::getId).distinct().toList());
        ids.addAll(anotherBothWayRuleAggrList.stream().map(ConfigurationRuleAggr::getId).distinct().toList());
        Date updateTime = new Date();
        List<ConfigurationRuleAggr> ruleAggrs = Lists.newArrayList();
        ids.stream().distinct().toList().forEach(id -> {
            ConfigurationRuleAggr configurationRuleAggr = new ConfigurationRuleAggr();
            configurationRuleAggr.setId(id);
            configurationRuleAggr.setChangeType(ConfigurationRuleChangeTypeEnum.REMOVE.getChangeType());
            configurationRuleAggr.setUpdateUser(userName);
            configurationRuleAggr.setUpdateTime(updateTime);
            ruleAggrs.add(configurationRuleAggr);
        });
        return ruleAggrs;
    }


    public static ConfigurationRuleAggr createUpdateInfo(Date effIn, Date effOut, String userName) {
        ConfigurationRuleAggr configurationRuleAggr=new ConfigurationRuleAggr();
        configurationRuleAggr.setUpdateUser(userName);
        if(Objects.nonNull(effIn)) {
            configurationRuleAggr.setEffIn(effIn);
        }
        if(Objects.nonNull(effOut)){
            configurationRuleAggr.setEffOut(effOut);
        }
        configurationRuleAggr.setUpdateUser(userName);
        return configurationRuleAggr;
    }

}
