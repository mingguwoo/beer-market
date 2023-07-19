package com.nio.ngfs.plm.bom.configuration.application.query.feature.assemble;

import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.QueryFeatureLibraryDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

/**
 * @author xiaozhou.tu
 * @date 2023/7/4
 */
public class FeatureLibraryAssembler {

    public static QueryFeatureLibraryDto assemble(BomsFeatureLibraryEntity entity) {
        QueryFeatureLibraryDto dto = new QueryFeatureLibraryDto();
        BeanUtils.copyProperties(entity, dto);
        if (CollectionUtils.isNotEmpty(entity.getChildren())) {
            dto.setChildren(LambdaUtil.map(entity.getChildren(), FeatureLibraryAssembler::assemble));
        }
        return dto;
    }

}
