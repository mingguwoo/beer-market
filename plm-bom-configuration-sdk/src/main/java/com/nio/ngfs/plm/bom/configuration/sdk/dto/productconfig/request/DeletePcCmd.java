package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xiaozhou.tu
 * @date 2023/8/15
 */
@Data
public class DeletePcCmd implements Cmd {

    @NotBlank(message = "PC Id is blank")
    private String pcId;

    @NotBlank(message = "Update User is blank")
    private String updateUser;

}
