package com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums;


import lombok.Getter;

/**
 * @author wangchao.wang
 */

@Getter
public enum OxoSnapshotEnum {

    FORMAL("Formal"),


    INFORMAL("Informal"),

    ;


    private String code;

    OxoSnapshotEnum(String code) {
        this.code = code;

    }



}
