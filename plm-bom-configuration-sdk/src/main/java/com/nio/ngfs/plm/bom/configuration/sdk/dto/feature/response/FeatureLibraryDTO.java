package com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response;

import lombok.Data;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Data
public class FeatureLibraryDTO {

    private String featureCode;

    private List<FeatureLibraryDTO> childrenList;

}
