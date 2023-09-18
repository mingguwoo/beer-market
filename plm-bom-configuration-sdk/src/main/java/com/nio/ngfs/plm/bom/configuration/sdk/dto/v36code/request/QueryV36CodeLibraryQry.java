package com.nio.ngfs.plm.bom.configuration.sdk.dto.v36code.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/9/18
 */
@Data
public class QueryV36CodeLibraryQry implements Qry {

    private String name;

    private String salesFeature;
}
