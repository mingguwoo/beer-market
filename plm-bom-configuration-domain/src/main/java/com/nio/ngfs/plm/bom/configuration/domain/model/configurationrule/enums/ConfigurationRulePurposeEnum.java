package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums;

import com.google.common.collect.Lists;
import com.nio.bom.share.utils.EnumUtils;
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
    )),
    SALES_TO_SALES(2, "Sales —> Sales", false, true, ConfigurationRuleTypeEnum.INCLUSIVE, Lists.newArrayList(
            RuleOptionMatrixValueEnum.INCLUSIVE, RuleOptionMatrixValueEnum.UNAVAILABLE
    )),
    SALES_INCLUSIVE_SALES(3, "Sales <—> Sales", true, true, ConfigurationRuleTypeEnum.INCLUSIVE, Lists.newArrayList(
            RuleOptionMatrixValueEnum.INCLUSIVE, RuleOptionMatrixValueEnum.UNAVAILABLE
    )),
    SALES_EXCLUSIVE_SALES(4, "Sales X Sales", true, true, ConfigurationRuleTypeEnum.EXCLUSIVE, Lists.newArrayList(
            RuleOptionMatrixValueEnum.EXCLUSIVE, RuleOptionMatrixValueEnum.UNAVAILABLE
    )),
    BASE_VEHICLE_TO_SALES(5, "Base Vehicle —> Sales", false, false, null, null);

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

    ConfigurationRulePurposeEnum(Integer code, String purpose, boolean bothWay, boolean selectOption,
                                 ConfigurationRuleTypeEnum ruleType, List<RuleOptionMatrixValueEnum> matrixValueList) {
        this.code = code;
        this.purpose = purpose;
        this.bothWay = bothWay;
        this.selectOption = selectOption;
        this.ruleType = ruleType;
        this.matrixValueList = matrixValueList;
    }

    public static ConfigurationRulePurposeEnum getByCode(Integer code) {
        return EnumUtils.getEnum(ConfigurationRulePurposeEnum.class, ConfigurationRulePurposeEnum::getCode, code);
    }

}
