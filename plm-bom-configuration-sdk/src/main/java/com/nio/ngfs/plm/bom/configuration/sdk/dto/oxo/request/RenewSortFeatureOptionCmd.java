package com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Cmd;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Data
public class RenewSortFeatureOptionCmd implements Cmd {

    private String modelCode;

}
