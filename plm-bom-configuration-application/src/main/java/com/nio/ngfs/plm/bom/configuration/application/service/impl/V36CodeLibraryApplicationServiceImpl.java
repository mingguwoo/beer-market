package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.google.common.base.Splitter;
import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.service.V36CodeLibraryApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureCatalogEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.v36code.V36CodeLibraryAggr;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/9/15
 */
@Service
@RequiredArgsConstructor
public class V36CodeLibraryApplicationServiceImpl implements V36CodeLibraryApplicationService {

    private final FeatureRepository featureRepository;

    @Override
    public void checkSalesFeatureList(V36CodeLibraryAggr aggr) {
        if (StringUtils.isBlank(aggr.getSalesFeatureList())) {
            return;
        }
        List<String> featureCodeList = Splitter.on(",").splitToList(aggr.getSalesFeatureList());
        List<FeatureAggr> featureAggrList = featureRepository.queryByFeatureOptionCodeList(featureCodeList);
        if (featureCodeList.size() != featureAggrList.size()) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        if (!featureAggrList.stream().allMatch(i -> i.isFeature() &&
                Objects.equals(FeatureCatalogEnum.SALES.getCatalog(), i.getCatalog()))) {
            throw new BusinessException(ConfigErrorCode.V36_CODE_SALES_FEATURE_NOT_MATCH);
        }
    }

    @Override
    public boolean isV36CodeIdInReleasedV36(V36CodeLibraryAggr aggr) {
        // todo
        return true;
    }

    @Override
    public Map<String, FeatureAggr> queryAllSalesFeature() {
        List<FeatureAggr> aggrList = featureRepository.queryFeatureByCatalog(FeatureCatalogEnum.SALES.getCatalog());
        Map<String,FeatureAggr> featureMap = new HashMap<>();
        aggrList.forEach(aggr->{
            featureMap.put(aggr.getFeatureCode(),aggr);
        });
        return featureMap;
    }

}
