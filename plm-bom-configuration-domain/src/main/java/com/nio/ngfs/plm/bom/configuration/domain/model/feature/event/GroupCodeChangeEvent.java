package com.nio.ngfs.plm.bom.configuration.domain.model.feature.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GroupCode变更事件
 *
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCodeChangeEvent extends DomainEvent {

    /**
     * Group
     */
    private FeatureAggr group;

    /**
     * 老的Group Code
     */
    private String oldGroupCode;

}
