package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import com.nio.ngfs.plm.bom.configuration.domain.facade.ColorPartFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.GetColorCodeReqDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.response.GetColorCodeRespDto;
import com.nio.ngfs.plm.bom.configuration.remote.FeishuIntegrationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/6
 */
@Component
@RequiredArgsConstructor
public class ColorPartFacadeImpl implements ColorPartFacade {

    private final FeishuIntegrationClient feishuIntegrationClient;

    @Override
    public GetColorCodeRespDto getColorCode(GetColorCodeReqDto dto) {
        feishuIntegrationClient.sendMessageToFeishu("");
        return null;
    }

}
