package com.nio.ngfs.plm.bom.configuration.application.command.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.event.EventPublisher;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.event.PcDeleteEvent;
import com.nio.ngfs.plm.bom.configuration.domain.service.productconfig.ProductConfigDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.DeletePcCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.DeletePcRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 删除PC
 *
 * @author xiaozhou.tu
 * @date 2023/8/15
 */
@Component
@RequiredArgsConstructor
public class DeletePcCommand extends AbstractLockCommand<DeletePcCmd, DeletePcRespDto> {

    private final ProductConfigRepository productConfigRepository;
    private final ProductConfigDomainService productConfigDomainService;
    private final EventPublisher eventPublisher;

    @Override
    protected String getLockKey(DeletePcCmd cmd) {
        return RedisKeyConstant.PRODUCT_CONFIG_DELETE_PC_LOCK_KEY_PREFIX + cmd.getPcId();
    }

    @Override
    protected DeletePcRespDto executeWithLock(DeletePcCmd cmd) {
        // 获取并校验聚合根
        ProductConfigAggr productConfigAggr = productConfigDomainService.getAndCheckAggr(cmd.getPcId());
        // 删除PC
        productConfigAggr.delete(cmd.getUpdateUser());
        // 数据库逻辑删除
        productConfigRepository.remove(productConfigAggr);
        // 发布PC删除事件
        eventPublisher.publish(new PcDeleteEvent(productConfigAggr));
        return new DeletePcRespDto();
    }

}
