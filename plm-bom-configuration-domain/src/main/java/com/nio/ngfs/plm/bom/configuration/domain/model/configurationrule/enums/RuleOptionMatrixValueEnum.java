package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums;

import com.nio.bom.share.utils.EnumUtils;
import lombok.Getter;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Getter
public enum RuleOptionMatrixValueEnum {

    INCLUSIVE(1, "Inclusive"),
    EXCLUSIVE(2, "Exclusive"),
    UNAVAILABLE(3, "Unavailable");

    private final Integer code;

    private final String matrixValue;

    RuleOptionMatrixValueEnum(Integer code, String matrixValue) {
        this.code = code;
        this.matrixValue = matrixValue;
    }

    public static RuleOptionMatrixValueEnum getByCode(Integer code) {
        return EnumUtils.getEnum(RuleOptionMatrixValueEnum.class, RuleOptionMatrixValueEnum::getCode, code);
    }

}
