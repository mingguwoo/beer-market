package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Data
public class RenewSortFeatureOptionCmd implements Cmd {

    @NotBlank(message = "Model Code is blank")
    private String modelCode;

    /**
     * 目标Feature Code
     */
    @NotBlank(message = "Target Feature Code is blank")
    private String targetFeatureCode;

    /**
     * 移动的Feature Code列表
     */
    @NotEmpty(message = "Move Feature Code List is empty")
    private List<String> moveFeatureCodeList;

}
