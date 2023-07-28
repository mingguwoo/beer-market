package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author xiaozhou.tu
 * @date 2023/7/28
 */
@Service
@RequiredArgsConstructor
public class OxoFeatureOptionApplicationServiceImpl implements OxoFeatureOptionApplicationService {

    private final FeatureRepository featureRepository;
    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;

    @Override
    public List<OxoFeatureOptionAggr> querySameSortGroupFeatureOption(String model, String featureOptionCode) {
        FeatureAggr featureAggr = featureRepository.getByFeatureOrOptionCode(featureOptionCode);
        if (featureAggr == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        List<FeatureAggr> sameSortGroupFeatureAggrList;
        if (featureAggr.isType(FeatureTypeEnum.FEATURE)) {
            sameSortGroupFeatureAggrList = featureRepository.queryByParentFeatureCodeAndType(
                            featureAggr.getParentFeatureCode(), FeatureTypeEnum.FEATURE.getType())
                    .stream().filter(i -> Objects.equals(i.getCatalog(), featureAggr.getCatalog())).toList();
        } else {
            sameSortGroupFeatureAggrList = featureRepository.queryByParentFeatureCodeAndType(
                    featureAggr.getParentFeatureCode(), FeatureTypeEnum.OPTION.getType());
        }
        List<String> featureOptionCodeList = LambdaUtil.map(sameSortGroupFeatureAggrList, i -> i.getFeatureId().getFeatureCode());
        return oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(model, featureOptionCodeList);
    }

}
