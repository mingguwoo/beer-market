package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import com.nio.bom.share.enums.CommonErrorCode;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.result.ResultInfo;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.facade.ModelFacade;
import com.nio.ngfs.plm.bom.configuration.remote.BomMiddlePlatformClient;
import com.nio.ngfs.plm.bom.configuration.remote.dto.platform.ModelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/8/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ModelFacadeImpl implements ModelFacade {

    private final BomMiddlePlatformClient bomMiddlePlatformClient;

    @Override
    public String getBrandByModel(String model) {
        ResultInfo<ModelDto> response;
        try {
            response = bomMiddlePlatformClient.getModel(model);
        } catch (Exception e) {
            log.error("bomMiddlePlatformClient getModel error", e);
            throw new BusinessException(CommonErrorCode.THIRD_PARTY_ERROR, e.getMessage());
        }
        if (response == null || !response.isSuccess()) {
            throw new BusinessException(ConfigErrorCode.BOM_MIDDLE_PLATFORM_GET_MODEL_FAIL);
        }
        if (response.getData() == null) {
            throw new BusinessException(ConfigErrorCode.BOM_MIDDLE_PLATFORM_MODEL_NOT_EXIST);
        }
        return response.getData().getBrand();
    }

}
