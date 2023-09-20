package com.nio.ngfs.plm.bom.configuration.domain.model.v36code.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * V36 Code属性变更事件
 *
 * @author xiaozhou.tu
 * @date 2023/9/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class V36CodeLibraryAttributeChangeEvent extends DomainEvent {

    private V36CodeLibraryAggr beforeAggr;

    private V36CodeLibraryAggr afterAggr;

}
