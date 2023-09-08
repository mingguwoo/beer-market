package com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ProductConfig打点变更事件
 *
 * @author xiaozhou.tu
 * @date 2023/9/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductConfigOptionChangeEvent extends DomainEvent {

    private List<ProductConfigAggr> productConfigAggrList;

    private List<ProductConfigOptionAggr> productConfigOptionAggrList;

}
