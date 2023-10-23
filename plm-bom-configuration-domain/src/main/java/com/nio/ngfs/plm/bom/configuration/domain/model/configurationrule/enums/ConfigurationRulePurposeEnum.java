package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums;

import com.google.common.collect.Lists;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.EnumUtils;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import lombok.Getter;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Getter
public enum ConfigurationRulePurposeEnum {

    SALES_TO_ENG(1, "Sales —> Eng", false, true, ConfigurationRuleTypeEnum.INCLUSIVE, Lists.newArrayList(
            RuleOptionMatrixValueEnum.INCLUSIVE, RuleOptionMatrixValueEnum.UNAVAILABLE
    ), true, true),
    SALES_TO_SALES(2, "Sales —> Sales", false, true, ConfigurationRuleTypeEnum.INCLUSIVE, Lists.newArrayList(
            RuleOptionMatrixValueEnum.INCLUSIVE, RuleOptionMatrixValueEnum.UNAVAILABLE
    ), true, true),
    SALES_INCLUSIVE_SALES(3, "Sales <—> Sales", true, true, ConfigurationRuleTypeEnum.INCLUSIVE, Lists.newArrayList(
            RuleOptionMatrixValueEnum.INCLUSIVE, RuleOptionMatrixValueEnum.UNAVAILABLE
    ), true, true),
    SALES_EXCLUSIVE_SALES(4, "Sales X Sales", true, true, ConfigurationRuleTypeEnum.EXCLUSIVE, Lists.newArrayList(
            RuleOptionMatrixValueEnum.EXCLUSIVE, RuleOptionMatrixValueEnum.UNAVAILABLE
    ), true, true),
    BASE_VEHICLE_TO_SALES(5, "Base Vehicle —> Sales", false, false, null, null, false, false);

    private final Integer code;

    private final String purpose;

    /**
     * 是否双向
     */
    private final boolean bothWay;

    /**
     * 是否前端可选项
     */
    private final boolean selectOption;

    private final ConfigurationRuleTypeEnum ruleType;

    private final List<RuleOptionMatrixValueEnum> matrixValueList;

    /**
     * 是否可删除Group
     */
    private final boolean canDeleteGroup;

    /**
     * 是否可删除Rule
     */
    private final boolean canDeleteRule;

    ConfigurationRulePurposeEnum(Integer code, String purpose, boolean bothWay, boolean selectOption,
                                 ConfigurationRuleTypeEnum ruleType, List<RuleOptionMatrixValueEnum> matrixValueList,
                                 boolean canDeleteGroup, boolean canDeleteRule) {
        this.code = code;
        this.purpose = purpose;
        this.bothWay = bothWay;
        this.selectOption = selectOption;
        this.ruleType = ruleType;
        this.matrixValueList = matrixValueList;
        this.canDeleteGroup = canDeleteGroup;
        this.canDeleteRule = canDeleteRule;
    }

    public static ConfigurationRulePurposeEnum getByCode(Integer code) {
        return EnumUtils.getEnum(ConfigurationRulePurposeEnum.class, ConfigurationRulePurposeEnum::getCode, code);
    }

    public static ConfigurationRulePurposeEnum getAndCheckByCode(Integer code) {
        ConfigurationRulePurposeEnum purposeEnum = getByCode(code);
        if (purposeEnum == null) {
            throw new BusinessException(ConfigErrorCode.CONFIGURATION_RULE_PURPOSE_ERROR);
        }
        return purposeEnum;
    }

}
