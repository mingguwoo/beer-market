package com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PC更新事件
 *
 * @author xiaozhou.tu
 * @date 2023/8/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigUpdateEvent extends DomainEvent {

    private String pcId;

    private String name;

}
