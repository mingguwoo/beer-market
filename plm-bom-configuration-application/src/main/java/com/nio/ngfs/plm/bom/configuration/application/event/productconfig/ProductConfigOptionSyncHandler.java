package com.nio.ngfs.plm.bom.configuration.application.event.productconfig;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.event.EventHandler;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ProductConfigFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncSelectPcOptionDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.SyncUnselectPcOptionDto;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.event.ProductConfigOptionChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ProductConfig打点同步到3DE
 *
 * @author xiaozhou.tu
 * @date 2023/9/8
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConfigOptionSyncHandler implements EventHandler<ProductConfigOptionChangeEvent> {

    private final ProductConfigFacade productConfigFacade;

    @Override
//    @Async("commonThreadPool")
    public void onApplicationEvent(@NotNull ProductConfigOptionChangeEvent event) {
        if (CollectionUtils.isEmpty(event.getProductConfigOptionAggrList())) {
            return;
        }
        Map<Long, ProductConfigAggr> productConfigAggrMap = LambdaUtil.toKeyMap(event.getProductConfigAggrList(), ProductConfigAggr::getId);
        event.getProductConfigOptionAggrList().forEach(aggr -> {
            try {
                ProductConfigAggr productConfigAggr = productConfigAggrMap.get(aggr.getPcId());
                if (productConfigAggr == null) {
                    throw new BusinessException(ConfigErrorCode.PRODUCT_CONFIG_PC_NOT_EXIST);
                }
                syncToEnovia(productConfigAggr, aggr);
            } catch (Exception e) {
                log.error("ProductConfigOptionSyncHandler syncToEnovia error", e);
            }
        });
    }

    private void syncToEnovia(ProductConfigAggr productConfigAggr, ProductConfigOptionAggr productConfigOptionAggr) {
        if (productConfigOptionAggr.isSelect()) {
            // 同步ProductConfig勾选到3DE
            SyncSelectPcOptionDto syncDto = new SyncSelectPcOptionDto();
            syncDto.setPcId(productConfigAggr.getPcId());
            syncDto.setOptionCode(productConfigOptionAggr.getOptionCode());
            syncDto.setFeatureCode(productConfigOptionAggr.getFeatureCode());
            productConfigFacade.syncSelectPcOptionToEnovia(syncDto);
        } else {
            // 同步ProductConfig取消勾选到3DE
            SyncUnselectPcOptionDto syncDto = new SyncUnselectPcOptionDto();
            syncDto.setPcId(productConfigAggr.getPcId());
            syncDto.setOptionCode(productConfigOptionAggr.getOptionCode());
            syncDto.setFeatureCode(productConfigOptionAggr.getFeatureCode());
            productConfigFacade.syncUnselectPcOptionToEnovia(syncDto);
        }
    }

}
