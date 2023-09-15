package com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Data
public class AddDigitCmd implements Cmd {

    @NotBlank(message = "Code is blank")
    private String code;

    @NotBlank(message = "Display Name is blank")
    @Size(max = 128, message = "Display Name max length is 128")
    private String displayName;

    @NotBlank(message = "Chinese Name is blank")
    @Size(max = 128, message = "Chinese Name max length is 128")
    private String chineseName;

    private List<String> salesFeatureCodeList;

    @Size(max = 1024, message = "Remark max length is 128")
    private String remark;

    @NotBlank(message = "Create User is blank")
    private String createUser;

}
