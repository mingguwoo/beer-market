package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/7/12
 */
@Data
public class EditFeatureCmd implements Cmd {

    @NotBlank(message = "Group Code is blank")
    @Size(max = 128, message = "Group Code max length is 128")
    private String groupCode;

    @NotBlank(message = "Feature Code is blank")
    @Size(max = 128, message = "Feature Code max length is 128")
    private String featureCode;

    @NotBlank(message = "Display Name is blank")
    @Size(max = 128, message = "Display Name max length is 128")
    private String displayName;

    @NotBlank(message = "Chinese Name is blank")
    @Size(max = 128, message = "Chinese Name max length is 128")
    private String chineseName;

    @Size(max = 128, message = "Description Name max length is 128")
    private String description;

    @NotBlank(message = "Catalog is blank")
    private String catalog;

    @NotBlank(message = "Requestor is blank")
    private String requestor;

    @NotBlank(message = "Update User is blank")
    private String updateUser;

}
