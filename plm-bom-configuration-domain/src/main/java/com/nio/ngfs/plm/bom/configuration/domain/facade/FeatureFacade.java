package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.FeatureOptionSyncReqDto;

/**
 * @author xiaozhou.tu
 * @date 2023/7/19
 */
public interface FeatureFacade {

    /**
     * 同步Feature/Option到3DE
     *
     * @param reqDto 同步数据
     */
    void syncFeatureOption(FeatureOptionSyncReqDto reqDto);

}
