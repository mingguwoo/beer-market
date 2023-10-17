package com.nio.ngfs.plm.bom.configuration.domain.model.configurationrule.enums;

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

}
