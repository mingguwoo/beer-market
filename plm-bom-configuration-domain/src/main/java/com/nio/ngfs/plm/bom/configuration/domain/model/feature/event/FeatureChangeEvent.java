package com.nio.ngfs.plm.bom.configuration.domain.model.feature.event;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureChangeTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
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
public class FeatureChangeEvent extends DomainEvent {

    /**
     * 变更的Feature
     */
    private FeatureAggr changedFeatureAggr;

    /**
     * 变更类型
     */
    private FeatureChangeTypeEnum changeType;

    public FeatureChangeEvent(FeatureAggr changedFeatureAggr) {
        this.changedFeatureAggr = changedFeatureAggr;
        if (changedFeatureAggr.isType(FeatureTypeEnum.GROUP)) {
            this.changeType = isAdd(changedFeatureAggr) ? FeatureChangeTypeEnum.GROUP_ADD : FeatureChangeTypeEnum.GROUP_UPDATE;
        } else if (changedFeatureAggr.isType(FeatureTypeEnum.FEATURE)) {
            this.changeType = isAdd(changedFeatureAggr) ? FeatureChangeTypeEnum.FEATURE_ADD : FeatureChangeTypeEnum.FEATURE_UPDATE;
        } else if (changedFeatureAggr.isType(FeatureTypeEnum.OPTION)) {
            this.changeType = isAdd(changedFeatureAggr) ? FeatureChangeTypeEnum.OPTION_ADD : FeatureChangeTypeEnum.OPTION_UPDATE;
        } else {
            throw new BusinessException(ConfigErrorCode.FEATURE_TYPE_INVALID);
        }
    }

    /**
     * 是否新增
     */
    private boolean isAdd(FeatureAggr changedFeatureAggr) {
        return changedFeatureAggr.getId() == null;
    }

}
