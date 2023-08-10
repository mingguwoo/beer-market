package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Data
public class GetBasedOnPcListQry implements Qry {

    @NotBlank(message = "Model is blank")
    private String model;

    private String search;

}
