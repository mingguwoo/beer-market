package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.oxo.assemble.OxoInfoAssembler;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.constants.ConfigConstants;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.enums.OxoOptionPackageTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums.OxoSnapshotEnum;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoFeatureOptionDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoVersionSnapshotDomainService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBaseVehicleDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsProductConfigModelOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsBaseVehicleEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsProductConfigModelOptionEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private final OxoFeatureOptionDomainService featureOptionDomainService;
    private final OxoVersionSnapshotDomainService versionSnapshotDomainService;
    private final OxoVersionSnapshotRepository oxoVersionSnapshotRepository;
    private final BomsProductConfigModelOptionDao bomsProductConfigModelOptionDao;
    private final BomsBaseVehicleDao bomsBaseVehicleDao;


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
    public Set<String> queryExistFeatureOptionInLastedReleaseSnapshot(String modelCode, List<OxoFeatureOptionAggr> featureOptionAggrList) {
        if (CollectionUtils.isEmpty(featureOptionAggrList)) {
            return Collections.emptySet();
        }
        // 查询最新Release的OXO版本
        OxoVersionSnapshotAggr oxoVersionSnapshotAggr = oxoVersionSnapshotRepository.queryLastReleaseSnapshotByModel(modelCode, null);
        if (oxoVersionSnapshotAggr == null) {
            return Collections.emptySet();
        }
        OxoListQry oxoListQry = versionSnapshotDomainService.resolveSnapShot(oxoVersionSnapshotAggr.getOxoSnapshot());
        Set<String> existFeatureOptionCodeSet = Sets.newHashSet();
        Set<String> featureOptionCodeSet = featureOptionAggrList.stream().map(OxoFeatureOptionAggr::getFeatureCode).collect(Collectors.toSet());
        if (oxoListQry != null && CollectionUtils.isNotEmpty(oxoListQry.getOxoRowsResps())) {
            oxoListQry.getOxoRowsResps().forEach(feature -> {
                if (featureOptionCodeSet.contains(feature.getFeatureCode())) {
                    existFeatureOptionCodeSet.add(feature.getFeatureCode());
                }
                if (CollectionUtils.isNotEmpty(feature.getOptions())) {
                    feature.getOptions().forEach(option -> {
                        if (featureOptionCodeSet.contains(option.getFeatureCode())) {
                            existFeatureOptionCodeSet.add(option.getFeatureCode());
                        }
                    });
                }
            });
        }
        return existFeatureOptionCodeSet;
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
        List<String> optionCodeList = LambdaUtil.map(featureAggrList, FeatureAggr::getFeatureCode);
        Map<String, String> optionFeatureCodeMap = LambdaUtil.toKeyValueMap(featureAggrList, FeatureAggr::getFeatureCode, FeatureAggr::getParentFeatureCode);
        // 查询Option行列表
        List<OxoFeatureOptionAggr> optionAggrList = oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(modelCode, optionCodeList);
        Map<String, List<OxoFeatureOptionAggr>> optionAggrByFeatureMap = LambdaUtil.groupBy(optionAggrList, i -> optionFeatureCodeMap.get(i.getFeatureCode()));
        featureOptionAggrList.forEach(featureOptionAggr -> {
            if (!featureOptionAggr.isFeature()) {
                return;
            }
            featureOptionAggr.setChildren(optionAggrByFeatureMap.getOrDefault(featureOptionAggr.getFeatureCode(), Lists.newArrayList()));
            featureOptionAggr.getChildren().forEach(option -> option.setParent(featureOptionAggr));
        });
    }

    @Override
    public Pair<List<OxoOptionPackageAggr>, List<String>> checkAndDeleteOptionPackage(String modelCode, List<OxoFeatureOptionAggr> featureOptionAggrList) {
        if (CollectionUtils.isEmpty(featureOptionAggrList)) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }
        // 查询所有Option的打点信息
        List<OxoFeatureOptionAggr> optionAggrList = LambdaUtil.map(featureOptionAggrList, OxoFeatureOptionAggr::isOption, Function.identity());
        featureOptionAggrList.stream().filter(OxoFeatureOptionAggr::isFeature).forEach(featureAggr ->
                optionAggrList.addAll(featureAggr.getChildren())
        );
        List<Long> featureOptionIdList = LambdaUtil.map(optionAggrList, OxoFeatureOptionAggr::getId);
        List<OxoOptionPackageAggr> optionPackageAggrList = oxoOptionPackageRepository.queryByFeatureOptionIdsAndHeadIdsList(featureOptionIdList,Lists.newArrayList());
        // Option的打点置为-
        List<OxoOptionPackageAggr> updateOptionPackageAggrList = optionPackageAggrList.stream().filter(OxoOptionPackageAggr::deleteOptionPackage).toList();
        Map<Long, OxoFeatureOptionAggr> featureOptionMapById = LambdaUtil.toKeyMap(optionAggrList, OxoFeatureOptionAggr::getId);
        Map<DeleteFeatureOptionCheckTypeEnum, Set<String>> messageTypeMap = checkDeleteOptionPackage(optionPackageAggrList, featureOptionMapById, modelCode);
        // 组装结果提示信息
        List<String> messageList = LambdaUtil.map(messageTypeMap.entrySet(), entry -> String.format(entry.getKey().getMessageFormat(),
                Joiner.on(",").join(entry.getValue())));
        return Pair.of(updateOptionPackageAggrList, messageList);
    }


    private Map<DeleteFeatureOptionCheckTypeEnum, Set<String>> checkDeleteOptionPackage(List<OxoOptionPackageAggr> optionPackageAggrList, Map<Long, OxoFeatureOptionAggr> featureOptionMapById,
                                                                                        String modelCode) {
        Map<DeleteFeatureOptionCheckTypeEnum, Set<String>> messageTypeMap = Maps.newHashMap();
        // 打点按Option行分组
        Map<Long, List<OxoOptionPackageAggr>> optionPackageAggrMap = LambdaUtil.groupBy(optionPackageAggrList, OxoOptionPackageAggr::getFeatureOptionId);
        optionPackageAggrMap.forEach((featureOptionId, aggrList) -> {
            // Option的打点全为-，跳过
            if (aggrList.stream().allMatch(OxoOptionPackageAggr::isPackageUnavailable)) {
                return;
            }
            OxoFeatureOptionAggr featureOptionAggr = featureOptionMapById.get(featureOptionId);
            OxoFeatureOptionAggr parent = featureOptionAggr.getParent();
            if (parent != null) {
                // 勾选Feature的提示
                messageTypeMap.getOrDefault(DeleteFeatureOptionCheckTypeEnum.WORKING_FEATURE, Sets.newHashSet()).add(parent.getFeatureCode());
            } else {
                // 勾选Option的提示
                messageTypeMap.getOrDefault(DeleteFeatureOptionCheckTypeEnum.WORKING_OPTION, Sets.newHashSet()).add(featureOptionAggr.getFeatureCode());
            }
        });
        // 判断Option行在当前最新Formal版本OXO是否不全为"-"
        OxoVersionSnapshotAggr oxoVersionSnapshotAggr = oxoVersionSnapshotRepository.queryLastReleaseSnapshotByModel(modelCode, OxoSnapshotEnum.FORMAL);
        if (oxoVersionSnapshotAggr == null) {
            return messageTypeMap;
        }
        OxoListQry oxoListQry = versionSnapshotDomainService.resolveSnapShot(oxoVersionSnapshotAggr.getOxoSnapshot());
        if (oxoListQry == null || CollectionUtils.isEmpty(oxoListQry.getOxoRowsResps())) {
            return messageTypeMap;
        }
        oxoListQry.getOxoRowsResps().forEach(feature -> {
            if (CollectionUtils.isEmpty(feature.getOptions())) {
                return;
            }
            feature.getOptions().forEach(option -> {
                OxoFeatureOptionAggr featureOptionAggr = featureOptionMapById.get(option.getRowId());
                if (featureOptionAggr == null) {
                    return;
                }
                if (option.getPackInfos().stream().allMatch(i -> Objects.equals(i.getPackageCode(), OxoOptionPackageTypeEnum.UNAVAILABLE.getType()))) {
                    return;
                }
                OxoFeatureOptionAggr parent = featureOptionAggr.getParent();
                if (parent != null) {
                    // 勾选Feature的提示
                    messageTypeMap.getOrDefault(DeleteFeatureOptionCheckTypeEnum.RELEASE_FEATURE, Sets.newHashSet()).add(parent.getFeatureCode());
                } else {
                    // 勾选Option的提示
                    messageTypeMap.getOrDefault(DeleteFeatureOptionCheckTypeEnum.RELEASE_OPTION, Sets.newHashSet()).add(featureOptionAggr.getFeatureCode());
                }
            });
        });
        return messageTypeMap;
    }


    /**
     * 根据 model 查询没有选中的featureCode
     *
     * @param modelCode
     * @return
     */
    @Override
    public List<FeatureAggr> queryFeaturesByModel(String modelCode) {
        List<OxoFeatureOptionAggr> featureOptionEntities =
                oxoFeatureOptionRepository.queryByModelAndFeatureCodeList(modelCode, Lists.newArrayList());

        List<String> featureCodes = Lists.newArrayList();


        if (CollectionUtils.isNotEmpty(featureOptionEntities)) {
            featureCodes.addAll(featureOptionEntities.stream().filter(x -> StringUtils.equals(x.getType(), FeatureTypeEnum.OPTION.getType()))
                    .map(OxoFeatureOptionAggr::getFeatureCode).distinct().toList());
        }

        // 查询 featureCodes
        List<FeatureAggr> optionAggrs = featureRepository.findFeatureLibraryNotFeatureCodes(featureCodes);

        if (CollectionUtils.isNotEmpty(optionAggrs)) {
            List<FeatureAggr> featureAggrs = featureRepository.queryByFeatureOptionCodeList(
                    optionAggrs.stream().map(FeatureAggr::getParentFeatureCode).distinct().toList());
            optionAggrs.addAll(featureAggrs);
        }
        return optionAggrs;
    }


    @Override
    public List<String> checkRules(String modelCode) {
        List<String> messages = Lists.newArrayList();

        /**
         * OXO中是否存在
         * 在所有Base Vehicle下打点都为“-”的Option（通过Delete Code删除的Option排除在外）
         */
        List<String> optionCodes = featureOptionDomainService.checkOxoBasicVehicleOptions(modelCode);


        /**
         * 系统校验（软校验）：
         * 1.该Option是否应用于Status为Working的Configuration Rule中
         * 2.该Option是否在Product Configuration有勾选
         */

        if (CollectionUtils.isNotEmpty(optionCodes)) {
            //该Option是否在Product Configuration有勾选
            List<BomsProductConfigModelOptionEntity> entities =
                    bomsProductConfigModelOptionDao.queryProductConfigModelOptionByModelOrFeatureOrOptionCode(modelCode, null, null, optionCodes);

            if (CollectionUtils.isNotEmpty(entities)) {
                entities.forEach(entity -> {
                    messages.add(MessageFormat.format(ConfigConstants.PRODUCT_CONFIGURATION_ERROR, entity.getOptionCode()));
                });
            }
        }

        return messages;
    }


    /**
     * 更新 软删除 标识
     *
     * @param modelCode
     */
    @Override
    public void updateSoftDelete(String modelCode) {


        // 查询 is_delete数据
        List<OxoFeatureOptionAggr> oxoFeatureOptions = oxoFeatureOptionRepository.queryFeatureListsByModelAndSortDelete(modelCode, false);


        if (CollectionUtils.isEmpty(oxoFeatureOptions)) {
            return;
        }

        List<Long> rowIds = oxoFeatureOptions.stream().map(OxoFeatureOptionAggr::getId).distinct().toList();


        List<OxoOptionPackageAggr> oxoOptionPackageAggrs =
                oxoOptionPackageRepository.queryByBaseVehicleIds(rowIds);


        // 根据 行id 查询
        if (CollectionUtils.isNotEmpty(oxoOptionPackageAggrs)) {
            List<Long> headIds = oxoOptionPackageAggrs.stream().map(OxoOptionPackageAggr::getBaseVehicleId).distinct().toList();


            // 查询车型行 信息
            List<BomsBaseVehicleEntity> baseVehicleEntities = bomsBaseVehicleDao.queryByModelCodeAndIdsAndActive(headIds, modelCode);


            if (CollectionUtils.isEmpty(baseVehicleEntities)) {
                return;
            }

            List<Long> ids = baseVehicleEntities.stream().map(BomsBaseVehicleEntity::getId).distinct().toList();


            // 获取 headId 信息
            Map<Long, List<OxoOptionPackageAggr>> packageMaps =
                    oxoOptionPackageAggrs.stream().collect(Collectors.groupingBy(OxoOptionPackageAggr::getFeatureOptionId));

            List<Long> deleteIds = Lists.newArrayList();

            packageMaps.forEach((k, v) -> {

                // 如果不为- 则去除软删除
                if (v.stream().anyMatch(x -> ids.contains(x.getBaseVehicleId()) &&
                        !StringUtils.equals(x.getPackageCode(), OxoOptionPackageTypeEnum.UNAVAILABLE.getType()))) {

                    deleteIds.add(k);
                }
            });


            if (CollectionUtils.isNotEmpty(deleteIds)) {
                oxoFeatureOptionRepository.restoreOxoFeatureOptionByIds(deleteIds, 0);
            }

        }


    }

    private enum DeleteFeatureOptionCheckTypeEnum {

        /**
         * 删除Feature/Option行校验类型
         */
        WORKING_FEATURE("The Options In Feature %s Has Valid Assignment(Not \"-\")!"),
        WORKING_OPTION("Option %s Has Valid Assignment(Not \"-\")!"),
        RELEASE_FEATURE("The Options In Feature %s Has Valid Assignment(Not \"-\") In Latest Formal OXO!"),
        RELEASE_OPTION("Option %s Has Valid Assignment(Not \"-\") In Latest Formal OXO!");

        @Getter
        private final String messageFormat;

        DeleteFeatureOptionCheckTypeEnum(String messageFormat) {
            this.messageFormat = messageFormat;
        }

    }

}
