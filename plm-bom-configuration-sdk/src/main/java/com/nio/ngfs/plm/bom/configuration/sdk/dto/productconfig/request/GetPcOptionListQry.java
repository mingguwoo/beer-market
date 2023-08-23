package com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/8/23
 */
@Data
public class GetPcOptionListQry implements Qry {

    @NotBlank(message = "Model is blank")
    private String model;

    private List<String> modelYearList;

}
