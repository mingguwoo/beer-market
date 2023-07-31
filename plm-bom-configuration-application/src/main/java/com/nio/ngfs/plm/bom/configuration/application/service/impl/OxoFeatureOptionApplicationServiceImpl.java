package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.google.common.collect.Maps;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @author xiaozhou.tu
 * @date 2023/7/28
 */
@Service
@RequiredArgsConstructor
public class OxoFeatureOptionApplicationServiceImpl implements OxoFeatureOptionApplicationService {

    private final FeatureRepository featureRepository;
    private final OxoFeatureOptionRepository oxoFeatureOptionRepository;
    private final OxoOptionPackageRepository oxoOptionPackageRepository;

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
    public Set<String> queryExistFeatureOptionInLastedReleaseSnapshot(List<OxoFeatureOptionAggr> featureOptionAggrList) {
        return Sets.newHashSet();
    }

    @Override
    public void buildFeatureOptionWithChildren(List<OxoFeatureOptionAggr> featureOptionAggrList) {
        if (CollectionUtils.isEmpty(featureOptionAggrList)) {
            return;
        }
        // 车型
        String modelCode = featureOptionAggrList.get(0).getModelCode();
        List<String> featureCodeList = LambdaUtil.map(featureOptionAggrList, OxoFeatureOptionAggr::isFeature, OxoFeatureOptionAggr::getFeatureCode);
        // 查询Feature的Option列表
        List<FeatureAggr> featureAggrList = featureRepository.queryByParentFeatureCodeListAndType(featureCodeList, FeatureTypeEnum.OPTION.getType());
        List<String> optionCodeList = LambdaUtil.map(featureAggrList, i -> i.getFeatureId().getFeatureCode());
        Map<String, String> optionFeatureCodeMap = LambdaUtil.toKeyValueMap(featureAggrList, i -> i.getFeatureId().getFeatureCode(), FeatureAggr::getParentFeatureCode);
        // 查询Option行列表
        List<OxoFeatureOptionAggr> optionAggrList = oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(modelCode, optionCodeList);
        Map<String, List<OxoFeatureOptionAggr>> optionAggrByFeatureMap = LambdaUtil.groupBy(optionAggrList, i -> optionFeatureCodeMap.get(i.getFeatureCode()));
        featureOptionAggrList.forEach(featureOptionAggr -> {
            if (!featureOptionAggr.isFeature()) {
                return;
            }
            featureOptionAggr.setChildren(optionAggrByFeatureMap.get(featureOptionAggr.getFeatureCode()));
        });
    }

    @Override
    public Pair<List<OxoOptionPackageAggr>, List<String>> checkAndDeleteOptionPackage(List<OxoFeatureOptionAggr> featureOptionAggrList) {
        // 查询所有Option的打点信息
        Map<Long, OxoFeatureOptionAggr> parentFeatureMap = Maps.newHashMap();
        List<OxoFeatureOptionAggr> optionAggrList = LambdaUtil.map(featureOptionAggrList, OxoFeatureOptionAggr::isOption, Function.identity());
        optionAggrList.forEach(i -> parentFeatureMap.put(i.getId(), i));
        featureOptionAggrList.stream().filter(OxoFeatureOptionAggr::isFeature).forEach(featureAggr -> {
            if (featureAggr.isFeature() && CollectionUtils.isNotEmpty(featureAggr.getChildren())) {
                optionAggrList.addAll(featureAggr.getChildren());
                featureAggr.getChildren().forEach(i -> parentFeatureMap.put(i.getId(), featureAggr));
            }
        });
        List<Long> featureOptionIdList = LambdaUtil.map(optionAggrList, OxoFeatureOptionAggr::getId);
        List<OxoOptionPackageAggr> optionPackageAggrList = oxoOptionPackageRepository.queryByFeatureOptionIdList(featureOptionIdList);
        optionPackageAggrList = optionPackageAggrList.stream().filter(OxoOptionPackageAggr::deleteOptionPackage).toList();
        List<String> messageList = checkDeleteOptionPackage(optionPackageAggrList, parentFeatureMap);
        return Pair.of(optionPackageAggrList, messageList);
    }

    private List<String> checkDeleteOptionPackage(List<OxoOptionPackageAggr> optionPackageAggrList, Map<Long, OxoFeatureOptionAggr> parentFeatureMap) {
        List<String> messageList = Lists.newArrayList();
        Map<Long, List<OxoOptionPackageAggr>> optionPackageAggrMap = LambdaUtil.groupBy(optionPackageAggrList, OxoOptionPackageAggr::getFeatureOptionId);
        optionPackageAggrMap.forEach((featureOptionId, aggrList) -> {
            if (!aggrList.stream().allMatch(OxoOptionPackageAggr::isPackageUnavailable)) {
                OxoFeatureOptionAggr parentFeature = parentFeatureMap.get(featureOptionId);
                if (parentFeature.isFeature()) {
                    messageList.add("The Options In Feature " + parentFeature.getFeatureCode() + " Has Valid Assignment(Not \"-\")!");
                } else {
                    messageList.add("Option " + parentFeature.getFeatureCode() + " Has Valid Assignment(Not \"-\")!");
                }
            }
        });
        return messageList;
    }

}
