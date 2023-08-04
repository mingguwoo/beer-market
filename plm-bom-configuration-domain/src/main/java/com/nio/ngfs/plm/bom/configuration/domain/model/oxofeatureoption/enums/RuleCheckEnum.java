package com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.enums;


import lombok.Getter;

/**
 * @author wangchao.wang
 */

@Getter
public enum RuleCheckEnum {


    Y("Y"),


    N("N");


    private String code;


    RuleCheckEnum(String code) {
        this.code = code;
    }


}
