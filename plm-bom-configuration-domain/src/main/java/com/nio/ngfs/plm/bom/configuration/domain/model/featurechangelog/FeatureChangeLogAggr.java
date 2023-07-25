package com.nio.ngfs.plm.bom.configuration.domain.model.featurechangelog;

import com.nio.bom.share.domain.model.AggrRoot;
import com.nio.ngfs.plm.bom.configuration.domain.model.AbstractDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Feature属性变更记录
 *
 * @author xiaozhou.tu
 * @date 2023/7/25
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureChangeLogAggr extends AbstractDo implements AggrRoot<Long> {

    private Long featureId;

    private String changeAttribute;

    private String oldValue;

    private String newValue;

    private String type;

    @Override
    public Long getUniqId() {
        return id;
    }

}
