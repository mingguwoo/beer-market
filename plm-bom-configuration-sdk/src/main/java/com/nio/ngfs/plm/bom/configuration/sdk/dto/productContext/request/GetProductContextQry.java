package com.nio.ngfs.plm.bom.configuration.sdk.dto.productContext.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/8/10
 */
@Data
public class GetProductContextQry implements Qry {

    private String modelCode;

    private String featureCode;

    private String groupCode;

    private String cataLog;
}
