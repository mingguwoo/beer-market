package com.nio.ngfs.plm.bom.configuration.infrastructure.facade;

import com.nio.ngfs.plm.bom.configuration.domain.facade.FeatureFacade;
import com.nio.ngfs.plm.bom.configuration.domain.facade.dto.request.FeatureOptionSyncReqDto;
import org.springframework.stereotype.Component;

/**
 * @author xiaozhou.tu
 * @date 2023/7/19
 */
@Component
public class FeatureFacadeImpl implements FeatureFacade {

    @Override
    public boolean isGroupExistedInGroupLibrary(String groupCode) {
        return true;
    }

    @Override
    public void syncFeatureOption(FeatureOptionSyncReqDto reqDto) {
    }

}
