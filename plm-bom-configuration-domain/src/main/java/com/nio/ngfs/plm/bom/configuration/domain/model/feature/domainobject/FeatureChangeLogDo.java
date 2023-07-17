package com.nio.ngfs.plm.bom.configuration.domain.model.feature.domainobject;

import com.nio.bom.share.domain.model.Entity;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureChangeLogDo extends AbstractDo implements Entity<Long> {

    private Long featureId;

    private String changeAttribute;

    private String oldValue;

    private String newValue;

    @Override
    public Long getUniqId() {
        return id;
    }

}
