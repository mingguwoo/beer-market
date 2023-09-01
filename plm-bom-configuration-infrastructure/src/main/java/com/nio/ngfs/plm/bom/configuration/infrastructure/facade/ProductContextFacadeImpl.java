package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import com.nio.bom.share.utils.GsonUtils;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductContextFacade;
import com.nio.ngfs.plm.bom.configuration.infrastructure.common.warn.ConfigurationTo3deWarnSender;
import com.nio.ngfs.plm.bom.configuration.infrastructure.facade.common.AbstractEnoviaFacade;
import com.nio.ngfs.plm.bom.configuration.remote.PlmEnoviaClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureDto;
import com.nio.ngfs.plm.bom.configuration.remote.dto.enovia.SyncProductContextModelFeatureOptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author bill.wang
 * @date 2023/8/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductContextFacadeImpl extends AbstractEnoviaFacade implements ProductContextFacade {

    private final PlmEnoviaClient plmEnoviaClient;
    private final ConfigurationTo3deWarnSender configurationTo3deWarnSender;

    @Override
    public void syncAddProductContextModelFeatureOptionToEnovia(SyncProductContextModelFeatureOptionDto modelFeatureOption) {
            invokeEnovia(plmEnoviaClient::syncProductContextModelFeatureOption,modelFeatureOption,"PlmEnoviaClient.syncProductContextModelFeatureOption",(response,e)->{
                configurationTo3deWarnSender.sendSyncProductContextModelFeatureOptionWarn(modelFeatureOption, e != null ? e.getMessage() : GsonUtils.toJson(response));
            });
    }

    @Override
    public void syncAddProductContextModelFeatureToEnovia(SyncProductContextModelFeatureDto modelFeature) {
        log.info("ProductContextFacade syncProductContextModelFeatureToEnovia data={}", GsonUtils.toJson(modelFeature));
        invokeEnovia(plmEnoviaClient::syncProductContextModelFeature,modelFeature,"PlmEnoviaClient.syncProductContextModelFeature",(response,e)->{
            configurationTo3deWarnSender.sendSyncProductContextFeatureModelWarn(modelFeature,e != null ? e.getMessage() : GsonUtils.toJson(response));
        });
    }

}
