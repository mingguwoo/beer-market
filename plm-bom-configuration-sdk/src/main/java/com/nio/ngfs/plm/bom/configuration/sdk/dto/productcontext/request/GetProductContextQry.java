package com.nio.ngfs.plm.bom.configuration.sdk.dto.productcontext.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@Data
public class GetProductContextQry implements Qry {

    @NotBlank(message = "Model Code Is Blank")
    private String modelCode;

    //feature code / option code / displayName
    private String feature;

    //group code
    private List<String> groupCode;

    private String cataLog;
}
