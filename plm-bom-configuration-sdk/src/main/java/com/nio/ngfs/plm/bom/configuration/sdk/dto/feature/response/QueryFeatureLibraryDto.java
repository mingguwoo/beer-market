package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response;

import com.nio.ngfs.plm.bom.configuration.sdk.dto.common.Dto;
import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
public class QueryFeatureLibraryDto implements Dto {

    private String featureCode;

    private String displayName;

    private List<QueryFeatureLibraryDto> children;

}
