package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.bom.share.domain.model.Identifier;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaozhou.tu
 * @date 2023/7/11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureId implements Identifier {

    /**
     * Feature Code
     */
    private String featureCode;

    /**
     * 类型，Group/Feature/Option
     */
    private String type;

    public FeatureId(String featureCode, FeatureTypeEnum typeEnum) {
        this.featureCode = featureCode;
        this.type = typeEnum.getType();
    }

}
