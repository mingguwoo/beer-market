package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xiaozhou.tu
 * @date 2023/10/25
 */
@Data
public class RuleOptionDto implements Dto {

    @NotBlank(message = "Driving Option Code is blank")
    private String drivingOptionCode;

    @NotBlank(message = "Driving Feature Code is blank")
    private String drivingFeatureCode;

    @NotBlank(message = "Constrained Option Code is blank")
    private String constrainedOptionCode;

    @NotBlank(message = "Constrained Feature Code is blank")
    private String constrainedFeatureCode;

    @NotNull(message = "Matrix Value is null")
    private Integer matrixValue;

}
