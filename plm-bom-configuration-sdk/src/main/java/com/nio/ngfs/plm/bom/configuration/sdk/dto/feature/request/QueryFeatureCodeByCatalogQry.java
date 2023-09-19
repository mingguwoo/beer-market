package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

/**
 * @author bill.wang
 * @date 2023/9/19
 */
@Data
public class QueryFeatureCodeByCatalogQry implements Qry {

    private String catalog;

}
