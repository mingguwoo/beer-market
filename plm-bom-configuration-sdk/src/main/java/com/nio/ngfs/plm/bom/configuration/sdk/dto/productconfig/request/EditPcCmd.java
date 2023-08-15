package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author xiaozhou.tu
 * @date 2023/8/14
 */
@Data
public class EditPcCmd implements Cmd {

    @NotBlank(message = "PC Id is blank")
    private String pcId;

    @NotBlank(message = "Name is blank")
    @Size(max = 128, message = "Name max length is 128")
    private String name;

}
