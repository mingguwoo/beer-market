package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.domain.model.productcontext.event.SyncProductContextEvent;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmSyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.PlmSyncProductContextModelFeatureOptionDto;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
public interface ProductContextFacade {

    void syncAddProductContextToEnovia(SyncProductContextEvent event);

    /**
     * 下发model feature option到3de
     * @param modelFeatureOption
     */
    void syncAddProductContextModelFeatureOptionToEnovia(PlmSyncProductContextModelFeatureOptionDto modelFeatureOption);

    /**
     * 下发model feature到3de
     * @param modelFeature
     */
    void syncAddProductContextModelFeatureToEnovia(PlmSyncProductContextModelFeatureDto modelFeature);
}
