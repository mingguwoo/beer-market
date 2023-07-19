package com.nio.ngfs.plm.bom.configuration.common.outresult;

/**
 * @author wangchao.wang
 */

public enum ResultType {



    SUCCESS("WL-0000", "Success"),


    ;

    private String code;
    private String desc;

    private ResultType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
