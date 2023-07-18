package com.nio.ngfs.plm.bom.configuration.application.query.feature.assemble;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.FeatureLibraryDto;

/**
 * @author xiaozhou.tu
 * @date 2023/7/4
 */
public class FeatureDtoAssembler {

    public static FeatureLibraryDto assemble(FeatureAggr aggr) {
        FeatureLibraryDto dto = new FeatureLibraryDto();
        dto.setFeatureCode(aggr.getFeatureId().getFeatureCode());
        return dto;
    }

}
