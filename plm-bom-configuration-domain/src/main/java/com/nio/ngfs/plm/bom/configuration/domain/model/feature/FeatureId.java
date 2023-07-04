package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.bom.share.domain.model.Identifier;
import lombok.Builder;
import lombok.Data;

/**
 * @author xiaozhou.tu
 * @date 2023/7/4
 */
@Data
@Builder
public class FeatureId implements Identifier {

    private String featureCode;

    private String type;

}
