package com.nio.ngfs.plm.bom.configuration.application.command.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.productconfig.ProductConfigRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.productconfig.ProductConfigDomainService;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.EditPcCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.EditPcRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 编辑PC
 *
 * @author xiaozhou.tu
 * @date 2023/8/14
 */
@Component
@RequiredArgsConstructor
public class EditPcCommand extends AbstractLockCommand<EditPcCmd, EditPcRespDto> {

    private final ProductConfigRepository productConfigRepository;
    private final ProductConfigDomainService productConfigDomainService;

    @Override
    protected String getLockKey(EditPcCmd cmd) {
        return RedisKeyConstant.PRODUCT_CONFIG_EDIT_PC_LOCK_KEY_PREFIX + cmd.getPcId();
    }

    @Override
    protected EditPcRespDto executeWithLock(EditPcCmd cmd) {
        // 获取并校验聚合根
        ProductConfigAggr productConfigAggr = productConfigDomainService.getAndCheckAggr(cmd.getPcId());
        // 编辑PC
        productConfigAggr.edit(cmd);
        // 校验PC Name是否唯一
        productConfigDomainService.checkPcNameUnique(productConfigAggr);
        // 保存到数据库
        productConfigRepository.save(productConfigAggr);
        return new EditPcRespDto();
    }

}
