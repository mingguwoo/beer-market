package com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OXO发布Release版本事件
 *
 * @author xiaozhou.tu
 * @date 2023/9/6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OxoVersionSnapshotPublishEvent extends DomainEvent {

    private OxoVersionSnapshotAggr oxoVersionSnapshotAggr;

}
