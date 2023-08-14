package com.nio.ngfs.plm.bom.configuration.sdk.dto.basevehicle.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author bill.wang
 * @date 2023/7/27
 */
@Data
public class QueryCopyFromModelsQry implements Qry {

    @NotBlank(message = "Model Code Is Blank")
    private String modelCode;

}
