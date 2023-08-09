package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author wangchao.wang
 */
@Data
public class OxoBaseCmd implements Cmd, Qry {


    /**
     * 车型
     */
    private String modelCode;


    private String userName;


    private Integer offSet;


    private Integer pageSize;

    /**
     * 版本
     */
    private String  version= StringUtils.EMPTY;


    /**
     * 权限点查询
     */
    private List<String> permissionPoints;
}
