package com.nio.ngfs.plm.bom.configuration.application.command.productconfig;

import com.nio.ngfs.plm.bom.configuration.application.command.AbstractLockCommand;
import com.nio.ngfs.plm.bom.configuration.common.constants.RedisKeyConstant;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.EditProductConfigCmd;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.EditProductConfigRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 编辑Product Config
 *
 * @author xiaozhou.tu
 * @date 2023/8/25
 */
@Component
@RequiredArgsConstructor
public class EditProductConfigCommand extends AbstractLockCommand<EditProductConfigCmd, EditProductConfigRespDto> {

    @Override
    protected String getLockKey(EditProductConfigCmd cmd) {
        return RedisKeyConstant.PRODUCT_CONFIG_EDIT_PRODUCT_CONFIG_LOCK_KEY_PREFIX + cmd.getModel();
    }

    @Override
    protected EditProductConfigRespDto executeWithLock(EditProductConfigCmd cmd) {
        // 更新skipCheck
        // 新增或更新打点
        // 打点是否可编辑校验（selectCanEdit、Product Context）
        // skipCheck校验（skipCheck变更全量，skipCheck未变更按需）
        // Feature在Product Context有勾选，PC下至少勾选一个Feature下的Option
        return null;
    }

}
