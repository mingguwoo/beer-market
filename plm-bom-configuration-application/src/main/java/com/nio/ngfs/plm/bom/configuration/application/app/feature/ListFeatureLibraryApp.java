package com.nio.ngfs.plm.bom.configuration.application.app.feature;

import com.nio.ngfs.plm.bom.configuration.application.app.Application;
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
public class ListFeatureLibraryApp implements Application<ListFeatureLibraryRequest, List<FeatureLibraryDTO>> {

    private final FeatureRepository featureRepository;

    @Override
    public List<FeatureLibraryDTO> doAction(ListFeatureLibraryRequest listFeatureLibraryRequest) {
        List<FeatureAggr> featureAggrList = featureRepository.listFeatureLibrary();
        return null;
    }

}
