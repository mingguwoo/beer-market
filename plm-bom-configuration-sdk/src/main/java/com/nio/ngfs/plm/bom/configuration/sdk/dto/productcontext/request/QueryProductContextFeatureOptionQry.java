package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xiaozhou.tu
 * @date 2023/10/19
 */
@Data
public class QueryProductContextFeatureOptionQry implements Qry {

    @NotBlank(message = "Model is blank")
    private String model;

    private String modelYear;

}
