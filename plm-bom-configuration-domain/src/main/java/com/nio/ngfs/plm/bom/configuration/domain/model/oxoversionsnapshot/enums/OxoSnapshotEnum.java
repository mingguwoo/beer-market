package com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums;


import lombok.Getter;

/**
 * @author wangchao.wang
 */
@Getter
public enum OxoSnapshotEnum {

    FORMAL("Formal"),
    INFORMAL("Informal"),
    ;

    private final String code;

    OxoSnapshotEnum(String code) {
        this.code = code;

    }
}
