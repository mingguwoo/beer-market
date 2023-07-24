package com.nio.ngfs.plm.bom.configuration.domain.model.feature.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureChangeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Feature变更事件（包括Group、Feature、Option新增和更新）
 *
 * @author xiaozhou.tu
 * @date 2023/7/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureChangeEvent extends DomainEvent {

    /**
     * 变更的Feature
     */
    private FeatureAggr changedFeatureAggr;

    private FeatureChangeTypeEnum changeType;

}
