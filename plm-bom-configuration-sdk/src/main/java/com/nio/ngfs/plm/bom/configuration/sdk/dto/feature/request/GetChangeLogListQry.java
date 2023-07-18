package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author xiaozhou.tu
 * @date 2023/7/18
 */
@Data
public class GetChangeLogListQry implements Qry {

    @NotNull(message = "Feature Id is null")
    private Long featureId;

}
