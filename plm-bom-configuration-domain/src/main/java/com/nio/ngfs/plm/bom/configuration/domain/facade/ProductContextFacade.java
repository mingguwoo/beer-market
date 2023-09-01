package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureOptionDto;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
public interface ProductContextFacade {

    void syncAddProductContextModelFeatureOptionToEnovia(SyncProductContextModelFeatureOptionDto modelFeatureOption);

    void syncAddProductContextModelFeatureToEnovia(SyncProductContextModelFeatureDto modelFeature);
}
