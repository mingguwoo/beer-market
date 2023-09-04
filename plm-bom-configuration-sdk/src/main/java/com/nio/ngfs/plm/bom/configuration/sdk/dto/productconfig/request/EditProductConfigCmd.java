package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/25
 */
@Data
public class EditProductConfigCmd implements Cmd {

    @NotBlank(message = "Model is blank")
    private String model;

    @NotEmpty(message = "PC List is empty")
    private List<PcDto> pcList;

    @NotEmpty(message = "Update PC Option Config List is empty")
    private List<PcOptionConfigDto> updatePcOptionConfigList;

    @Data
    public static class PcDto {

        @NotBlank(message = "PC Id is blank")
        private String pcId;

        @NotBlank(message = "Skip Check is blank")
        private String skipCheck;

    }

    @Data
    public static class PcOptionConfigDto {

        @NotBlank(message = "PC Id is blank")
        private String pcId;

        @NotBlank(message = "Option Code is blank")
        private String optionCode;

        private boolean select;

    }

}