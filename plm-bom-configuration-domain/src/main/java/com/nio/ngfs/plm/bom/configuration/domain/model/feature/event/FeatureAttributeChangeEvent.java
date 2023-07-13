package com.nio.ngfs.plm.bom.configuration.domain.model.feature.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Feature属性变更事件
 *
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureAttributeChangeEvent extends DomainEvent {

    private FeatureAggr beforeFeatureAggr;

    private FeatureAggr afterFeatureAggr;

}
