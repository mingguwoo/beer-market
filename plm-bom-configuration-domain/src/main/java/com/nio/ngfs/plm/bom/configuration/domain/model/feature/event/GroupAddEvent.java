package com.nio.ngfs.plm.bom.configuration.domain.model.feature.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Group新增事件
 *
 * @author xiaozhou.tu
 * @date 2023/7/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupAddEvent extends DomainEvent {

    private String groupCode;

}
