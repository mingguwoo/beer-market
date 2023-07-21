package com.nio.ngfs.plm.bom.configuration.domain.model.feature.event;

import com.nio.bom.share.enums.StatusEnum;
import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Feature状态批量变更事件
 *
 * @author xiaozhou.tu
 * @date 2023/7/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureStatusChangeEvent extends DomainEvent {

    private List<Long> featureIdList;

    private StatusEnum oldStatus;

    private StatusEnum newStatus;

    private String updateUser;

}
