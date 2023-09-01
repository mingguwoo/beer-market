package com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event;

import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontextfeature.ProductContextFeatureAggr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncProductContextEvent extends DomainEvent {

    private List<ProductContextAggr> productContextAggrlist;

    private List<ProductContextFeatureAggr> productContextFeatureAggrList;

}
