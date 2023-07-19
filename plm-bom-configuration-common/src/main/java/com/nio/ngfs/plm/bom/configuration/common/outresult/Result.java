package com.nio.ngfs.plm.bom.configuration.common.outresult;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author wangchao.wang
 */
@Data
public class Result {


    private Object resultData;
    private String resultCode;
    private String resultDesc;


    public static Result successResult() {
        return result(ResultType.SUCCESS);
    }



    public static Result result(ResultType resultType) {
        Result result = new Result();
        result.setResultCode(resultType.getCode());
        result.setResultDesc(resultType.getDesc());
        return result;
    }


    @JsonIgnore
    public boolean isError() {
        return !ResultType.SUCCESS.getCode().equals(this.getResultCode());
    }

    @JsonIgnore
    public boolean isNormal() {
        return ResultType.SUCCESS.getCode().equals(this.getResultCode());
    }
}
