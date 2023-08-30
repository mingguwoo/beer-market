package com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event;

import cn.hutool.core.collection.CollectionUtil;
import com.nio.ngfs.plm.bom.configuration.domain.event.DomainEvent;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.ProductContextFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.ProductContextOptionDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureOptionDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.ProductContextAggr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncProductContextEvent extends DomainEvent {

    private List<ProductContextAggr> productContextAggrlist;

//    private List<SyncProductContextModelFeatureDto> modelFeatureList;
//
//    private SyncProductContextModelFeatureOptionDto featureOptionList;

}
