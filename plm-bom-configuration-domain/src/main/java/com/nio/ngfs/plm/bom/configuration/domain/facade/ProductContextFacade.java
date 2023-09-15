package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncProductContextModelFeatureOptionDto;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
public interface ProductContextFacade {

    /**
     * 下发model feature option到3de
     * @param modelFeatureOption
     */
    void syncAddProductContextModelFeatureOptionToEnovia(SyncProductContextModelFeatureOptionDto modelFeatureOption);

    /**
     * 下发model feature 到3de
     * @param modelFeature
     */
    void syncAddProductContextModelFeatureToEnovia(SyncProductContextModelFeatureDto modelFeature);
}
