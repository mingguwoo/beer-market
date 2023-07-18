package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

/**
 * @author wangchao.wang
 */
@Data
public class OxoBaseCmd implements Cmd {

    private String modelCode;

    private String userName;

    private Integer offSet;

    private Integer pageSize;
}
