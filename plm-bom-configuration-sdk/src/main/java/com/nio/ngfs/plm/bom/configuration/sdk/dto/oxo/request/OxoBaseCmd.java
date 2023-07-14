package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @author wangchao.wang
 */

@Data
public class OxoBaseCmd implements Cmd {


    private String modelCode;

    @NotBlank(message = "用户名称不能为空")
    private String userName;


    private Integer offSet;


    private Integer pageSize;
}
