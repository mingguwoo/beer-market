package com.nio.ngfs.plm.bom.configuration.application.query.feature;

import com.nio.ngfs.plm.bom.configuration.application.query.Query;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.ListFeatureLibraryRequest;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.FeatureLibraryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Component
@RequiredArgsConstructor
public class ListFeatureLibraryQuery implements Query<ListFeatureLibraryRequest, List<FeatureLibraryDTO>> {

    private final FeatureRepository featureRepository;

    @Override
    public List<FeatureLibraryDTO> doAction(ListFeatureLibraryRequest listFeatureLibraryRequest) {
        // 查询
        // 1、可以直接调Repository查询
        // 2、也可以调DomainService
        // 3、也可以抽一层QueryService（通用化，多个请求都需要用到）
        List<FeatureAggr> featureAggrList = featureRepository.listFeatureLibrary();
        return null;
    }

}
