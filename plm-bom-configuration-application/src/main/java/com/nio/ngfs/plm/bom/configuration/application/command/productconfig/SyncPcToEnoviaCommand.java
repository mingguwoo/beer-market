package com.nio.ngfs.plm.bom.configuration.application.command.productconfig;

import com.google.common.collect.Lists;
import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.event.PcAddEvent;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.ProductConfigOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfigoption.event.ProductConfigOptionChangeEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.productconfig.ProductConfigDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.SyncPcToEnoviaCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.SyncPcToEnoviaRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 同步PC和PC打点到3DE
 *
 * @author xiaozhou.tu
 * @date 2023/10/30
 */
@Component
@RequiredArgsConstructor
public class SyncPcToEnoviaCommand extends AbstractLockCommand<SyncPcToEnoviaCmd, SyncPcToEnoviaRespDto> {

    private final ProductConfigOptionRepository productConfigOptionRepository;
    private final ProductConfigDomainService productConfigDomainService;
    private final EventPublisher eventPublisher;

    @Override
    protected String getLockKey(SyncPcToEnoviaCmd cmd) {
        return RedisKeyConstant.PRODUCT_CONFIG_SYNC_TO_ENOVIA_LOCK_KEY_PREFIX + cmd.getPcId();
    }

    @Override
    protected SyncPcToEnoviaRespDto executeWithLock(SyncPcToEnoviaCmd cmd) {
        ProductConfigAggr productConfigAggr = productConfigDomainService.getAndCheckAggr(cmd.getPcId());
        // 同步新增PC到3DE，发布PC新增事件
        eventPublisher.publish(new PcAddEvent(productConfigAggr));
        // 同步PC打点到3DE
        List<ProductConfigOptionAggr> optionAggrList = productConfigOptionRepository.queryByPcId(productConfigAggr.getId());
        eventPublisher.publish(new ProductConfigOptionChangeEvent(Lists.newArrayList(productConfigAggr), optionAggrList));
        return new SyncPcToEnoviaRespDto();
    }

}
