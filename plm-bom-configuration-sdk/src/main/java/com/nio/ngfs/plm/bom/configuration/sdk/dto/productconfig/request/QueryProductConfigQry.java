package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/22
 */
@Data
public class QueryProductConfigQry implements Qry {

    @NotBlank(message = "Model is blank")
    private String model;

    private List<String> modelYearList;

    private List<String> groupList;

    private List<String> pcIdList;

    private String search;

    /**
     * 是否编辑模式
     */
    private boolean edit = false;

}
