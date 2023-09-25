package com.nio.ngfs.plm.bom.configuration.domain.model.oxo.enums;


/**
 * @author wangchao.wang
 */
public enum CompareChangeTypeEnum {


    ADD("Add"),
    DELETE("Delete"),
    MODIFY("Modify"),
    NO_CHANGE("No change"),


    DEL("Del");
    private final String name;

    CompareChangeTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
