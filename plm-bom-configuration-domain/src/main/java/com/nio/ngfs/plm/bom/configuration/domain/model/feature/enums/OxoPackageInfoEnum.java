package com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums;

import com.nio.bom.share.utils.EnumUtils;
import lombok.Getter;

/**
 * @author wangchao.wang
 */
@Getter
public enum OxoPackageInfoEnum {

    DEFALUT ("●","Default"),


    UNAVAILABLE("-","Unavailable"),


    AVAILABLE("○","Available"),
    ;


    private String code;

    private String desc;


    OxoPackageInfoEnum(String code,String desc) {
        this.code = code;
        this.desc = desc;
    }







}
