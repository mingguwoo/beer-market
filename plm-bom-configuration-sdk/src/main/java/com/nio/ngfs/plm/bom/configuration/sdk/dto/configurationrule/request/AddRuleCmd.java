package com.nio.ngfs.plm.bom.configuration.sdk.dto.configurationrule.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/10/17
 */
@Data
public class AddRuleCmd implements Cmd {

    @NotBlank(message = "Chinese Name is blank")
    @Size(max = 128, message = "Chinese Name max length is 128")
    private String chineseName;

    @Size(max = 128, message = "Display Name max length is 128")
    private String displayName;

    @NotNull(message = "Purpose is null")
    private Integer purpose;

    @NotBlank(message = "Defined By is blank")
    private String definedBy;

    @Size(max = 1024, message = "Description max length is 1024")
    private String description;

    private String drivingFeature;

    private List<String> constrainedFeatureList;

    private List<RuleOptionDto> ruleOptionList;

    @NotBlank(message = "Create User is blank")
    private String createUser;

    @Data
    public static class RuleOptionDto {

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

}
