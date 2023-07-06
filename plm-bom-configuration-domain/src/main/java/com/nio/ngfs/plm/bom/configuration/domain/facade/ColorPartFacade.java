package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.GetColorCodeReqDto;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.response.GetColorCodeRespDto;

/**
 * @author xiaozhou.tu
 * @date 2023/7/6
 */
public interface ColorPartFacade {

    /**
     * 获取颜色码
     *
     * @param dto dto
     * @return 颜色码
     */
    GetColorCodeRespDto getColorCode(GetColorCodeReqDto dto);

}
