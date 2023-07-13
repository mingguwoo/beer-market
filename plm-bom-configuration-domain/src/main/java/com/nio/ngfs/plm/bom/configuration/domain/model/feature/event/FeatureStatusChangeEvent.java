package com.nio.ngfs.plm.bom.configuration.domain.model.feature.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureStatusChangeEvent extends DomainEvent {

    private List<Long> featureIdList;

    private FeatureStatusEnum oldStatus;

    private FeatureStatusEnum newStatus;

    private String updateUser;

}
