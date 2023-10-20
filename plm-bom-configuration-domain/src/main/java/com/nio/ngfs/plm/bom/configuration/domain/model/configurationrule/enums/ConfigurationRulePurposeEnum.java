package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums;

import com.nio.bom.share.utils.EnumUtils;
import lombok.Getter;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Getter
public enum ConfigurationRulePurposeEnum {

    SALES_TO_ENG(1, "Sales —> Eng", true, ConfigurationRuleTypeEnum.INCLUSIVE),
    SALES_TO_SALES(2, "Sales —> Sales", true, ConfigurationRuleTypeEnum.INCLUSIVE),
    SALES_INCLUSIVE_SALES(3, "Sales <—> Sales", true, ConfigurationRuleTypeEnum.INCLUSIVE),
    SALES_EXCLUSIVE_SALES(4, "Sales X Sales", true, ConfigurationRuleTypeEnum.EXCLUSIVE),
    BASE_VEHICLE_TO_SALES(5, "Base Vehicle —> Sales", false, ConfigurationRuleTypeEnum.INCLUSIVE);

    private final Integer code;

    private final String purpose;

    /**
     * 是否前端可选项
     */
    private final boolean selectOption;

    private final ConfigurationRuleTypeEnum ruleType;

    ConfigurationRulePurposeEnum(Integer code, String purpose, boolean selectOption, ConfigurationRuleTypeEnum ruleType) {
        this.code = code;
        this.purpose = purpose;
        this.selectOption = selectOption;
        this.ruleType = ruleType;
    }

    public static ConfigurationRulePurposeEnum getByCode(Integer code) {
        return EnumUtils.getEnum(ConfigurationRulePurposeEnum.class, ConfigurationRulePurposeEnum::getCode, code);
    }

}
