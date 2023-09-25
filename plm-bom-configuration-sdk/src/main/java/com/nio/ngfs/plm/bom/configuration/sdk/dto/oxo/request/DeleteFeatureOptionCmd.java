package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/31
 */
@Data
public class DeleteFeatureOptionCmd implements Cmd {

    @NotBlank(message = "Model Code is blank")
    private String modelCode;

    @NotEmpty(message = "Feature Code List is empty")
    private List<String> featureCodeList;

    /**
     *  type check是校验 delete是删除
     */
    private String type;

}
