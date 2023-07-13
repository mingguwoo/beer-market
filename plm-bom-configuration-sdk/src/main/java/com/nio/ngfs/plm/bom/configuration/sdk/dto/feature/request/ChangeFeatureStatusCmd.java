package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
@Data
public class ChangeFeatureStatusCmd implements Cmd {

    @NotBlank(message = "Feature Code is blank")
    @Size(max = 128, message = "Feature Code max length is 128")
    private String featureCode;

    @NotBlank(message = "Status is blank")
    private String status;

}
