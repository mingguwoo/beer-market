package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums;

import com.nio.bom.share.utils.EnumUtils;
import lombok.Getter;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Getter
public enum ConfigurationRulePurposeEnum {

    SALES_TO_ENG(1, "Sales —> Eng"),
    SALES_TO_SALES(2, "Sales —> Sales"),
    SALES_INCLUSIVE_SALES(3, "Sales <—> Sales"),
    SALES_EXCLUSIVE_SALES(4, "Sales X Sales"),
    BASE_VEHICLE_TO_SALES(5, "Base Vehicle —> Sales");

    private final Integer code;

    private final String purpose;

    ConfigurationRulePurposeEnum(Integer code, String purpose) {
        this.code = code;
        this.purpose = purpose;
    }

    public static ConfigurationRulePurposeEnum getByCode(Integer code) {
        return EnumUtils.getEnum(ConfigurationRulePurposeEnum.class, ConfigurationRulePurposeEnum::getCode, code);
    }

}
