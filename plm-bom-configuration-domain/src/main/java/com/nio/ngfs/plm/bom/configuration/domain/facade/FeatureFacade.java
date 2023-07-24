package com.nio.ngfs.plm.bom.configuration.domain.facade;

import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.FeatureOptionSyncReqDto;

/**
 * @author xiaozhou.tu
 * @date 2023/7/19
 */
public interface FeatureFacade {

    /**
     * Group在3DE Group Library中是否存在
     *
     * @param groupCode Group Code
     * @return true|false
     */
    boolean isGroupExistedInGroupLibrary(String groupCode);

    /**
     * 同步Feature/Option到3DE
     *
     * @param reqDto 同步数据
     */
    void syncFeatureOption(FeatureOptionSyncReqDto reqDto);

}
