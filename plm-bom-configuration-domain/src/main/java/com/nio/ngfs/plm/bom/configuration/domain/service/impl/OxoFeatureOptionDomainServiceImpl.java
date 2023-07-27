package com.nio.ngfs.plm.bom.configuration.domain.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.OxoFeatureOptionDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OxoFeatureOptionDomainServiceImpl implements OxoFeatureOptionDomainService {

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

    @Override
    public void renewSortFeatureOption(List<OxoFeatureOptionAggr> oxoFeatureOptionAggrList, String targetFeatureCode, List<String> moveFeatureCodeList) {
        if (CollectionUtils.isEmpty(oxoFeatureOptionAggrList)) {
            return;
        }
        Set<String> existFeatureCodeSet = oxoFeatureOptionAggrList.stream().map(OxoFeatureOptionAggr::getFeatureCode).collect(Collectors.toSet());
        if (!existFeatureCodeSet.containsAll(moveFeatureCodeList)) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        // 排序
        oxoFeatureOptionAggrList = oxoFeatureOptionAggrList.stream().sorted(OxoFeatureOptionAggr::compareTo).toList();
        List<String> sortFeatureCodeList = LambdaUtil.map(oxoFeatureOptionAggrList, OxoFeatureOptionAggr::getFeatureCode);
        Set<String> moveFeatureCodeSet = Sets.newHashSet(moveFeatureCodeList);
        // 重排序Feature Code
        List<String> newSortFeatureCodeList = Lists.newArrayList();
        sortFeatureCodeList.forEach(featureCode -> {
            if (moveFeatureCodeSet.contains(featureCode)) {
                return;
            }
            newSortFeatureCodeList.add(featureCode);
            if (Objects.equals(featureCode, targetFeatureCode)) {
                newSortFeatureCodeList.addAll(moveFeatureCodeList);
            }
        });
        // 更新排序值sort
        Map<String, OxoFeatureOptionAggr> oxoFeatureOptionAggrMap = LambdaUtil.toKeyMap(oxoFeatureOptionAggrList, OxoFeatureOptionAggr::getFeatureCode);
        for (int i = 0; i < newSortFeatureCodeList.size(); i++) {
            oxoFeatureOptionAggrMap.get(newSortFeatureCodeList.get(i)).renewSort(i);
        }
    }

}
