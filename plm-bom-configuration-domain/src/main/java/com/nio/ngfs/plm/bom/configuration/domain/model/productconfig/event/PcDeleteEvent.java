package com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PC删除事件
 *
 * @author xiaozhou.tu
 * @date 2023/8/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PcDeleteEvent extends DomainEvent {

    private ProductConfigAggr productConfigAggr;

}
