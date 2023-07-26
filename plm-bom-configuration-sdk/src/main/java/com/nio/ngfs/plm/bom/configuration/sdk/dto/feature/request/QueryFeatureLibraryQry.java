package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Qry;
import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
public class QueryFeatureLibraryQry implements Qry {

    private List<String> groupCodeList;

    private String catalog;

    private String status;

    private String search;

    private boolean relatedModel = false;

}
