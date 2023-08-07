package com.nio.ngfs.plm.bom.configuration.application.service.impl;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.oxo.assemble.OxoInfoAssembler;
import com.nio.ngfs.plm.bom.configuration.application.service.OxoFeatureOptionApplicationService;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.FeatureRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureTypeEnum;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxofeatureoption.OxoFeatureOptionRepository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageRepository;
import com.nio.ngfs.plm.bom.configuration.domain.service.basevehicle.BaseVehicleDomainService;
import com.nio.ngfs.plm.bom.configuration.domain.service.oxo.OxoFeatureOptionDomainService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoFeatureOptionDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsOxoOptionPackageDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoFeatureOptionEntity;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoHeadQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoRowsQry;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

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
    private final BaseVehicleDomainService baseVehicleDomainService;
    private final OxoFeatureOptionDomainService featureOptionDomainService;


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
            featureOptionAggr.setChildren(optionAggrByFeatureMap.getOrDefault(featureOptionAggr.getFeatureCode(), Lists.newArrayList()));
            featureOptionAggr.getChildren().forEach(option -> option.setParent(featureOptionAggr));
        });
    }

    @Override
    public Pair<List<OxoOptionPackageAggr>, List<String>> checkAndDeleteOptionPackage(List<OxoFeatureOptionAggr> featureOptionAggrList) {
        // 查询所有Option的打点信息
        List<OxoFeatureOptionAggr> optionAggrList = LambdaUtil.map(featureOptionAggrList, OxoFeatureOptionAggr::isOption, Function.identity());
        featureOptionAggrList.stream().filter(OxoFeatureOptionAggr::isFeature).forEach(featureAggr ->
                optionAggrList.addAll(featureAggr.getChildren())
        );
        List<Long> featureOptionIdList = LambdaUtil.map(optionAggrList, OxoFeatureOptionAggr::getId);
        List<OxoOptionPackageAggr> optionPackageAggrList = oxoOptionPackageRepository.queryByFeatureOptionIdList(featureOptionIdList);
        // Option的打点置为-
        List<OxoOptionPackageAggr> updateOptionPackageAggrList = optionPackageAggrList.stream().filter(OxoOptionPackageAggr::deleteOptionPackage).toList();
        Map<Long, OxoFeatureOptionAggr> featureOptionMapById = LambdaUtil.toKeyMap(optionAggrList, OxoFeatureOptionAggr::getId);
        List<String> messageList = checkDeleteOptionPackage(optionPackageAggrList, featureOptionMapById);
        return Pair.of(updateOptionPackageAggrList, messageList);
    }

    @Override
    public OxoListQry queryOxoInfoByModelCode(String modelCode) {

        // 查询行数据
        List<OxoFeatureOptionAggr> oxoFeatureOptions = oxoFeatureOptionRepository.queryFeatureListsByModel(modelCode);

        //获取所有的行节点
        List<Long> rowIds = oxoFeatureOptions.stream().map(OxoFeatureOptionAggr::getId).distinct().toList();

        // 获取打点信息
        List<OxoOptionPackageAggr> entities =
                oxoOptionPackageRepository.queryByBaseVehicleIds(rowIds);

        //查询表头信息
        List<OxoHeadQry> oxoLists = baseVehicleDomainService.queryByModel(modelCode);

        /**
         * 系统默认排序
         * 1.Feature的Catalog属性（Engineering在前，Sales在后）
         * 2.Group排序（按首数字正排，00在前，依次往后排）
         * 3.Feature排序（按首字母正排）+Renew Sort按钮调整的Feature排序
         * 4.Feature下的Option排序（按首字母正排）+Renew Sort按钮调整的Option排序
         *
         * 自定义：
         * 基于某个Catalog（Engineering或Sales）,在同一Group下，可对Feature进行重新排序
         * 可对某个Feature下的Option进行重新排序
         */
        Map<String, List<OxoFeatureOptionAggr>> oxoInfoDoMaps =
                oxoFeatureOptions.stream().filter(x -> StringUtils.equals(x.getType(), FeatureTypeEnum.FEATURE.getType()))
                        .sorted(Comparator.comparing(OxoFeatureOptionAggr::getCatalog).thenComparing(OxoFeatureOptionAggr::getParentFeatureCode))
                        .collect(Collectors.groupingBy(OxoFeatureOptionAggr::getParentFeatureCode));

        OxoListQry qry=new OxoListQry();
        qry.setOxoHeadResps(oxoLists);

        List<OxoRowsQry> rowsQryList= com.google.common.collect.Lists.newArrayList();
        oxoInfoDoMaps.forEach((k, features) -> {
            List<OxoFeatureOptionAggr> oxoInfoDoList = features.stream().sorted().sorted(Comparator.comparing(OxoFeatureOptionAggr::getSort)
                    .thenComparing(OxoFeatureOptionAggr::getFeatureCode)).toList();

            oxoInfoDoList.forEach(x -> {
                //获取
                OxoRowsQry oxoRowsQry = OxoInfoAssembler.assembleOxoQry(x, k);

                List<OxoFeatureOptionAggr> options = oxoFeatureOptions.stream().filter(y ->
                                StringUtils.equals(y.getParentFeatureCode(), x.getFeatureCode()) &&
                                        StringUtils.equals(y.getType(), FeatureTypeEnum.OPTION.getType())).
                        sorted(Comparator.comparing(OxoFeatureOptionAggr::getSort).thenComparing(OxoFeatureOptionAggr::getFeatureCode)).toList();

                List<OxoRowsQry> optionQrys = com.google.common.collect.Lists.newArrayList();

                if (CollectionUtils.isNotEmpty(options)) {
                    options.forEach(option -> {
                        OxoRowsQry optionQry = OxoInfoAssembler.assembleOxoQry(option, k);

                        if (CollectionUtils.isNotEmpty(entities)) {
                            optionQry.setPackInfos(OxoInfoAssembler.buildOxoEditCmd(entities,option,oxoLists));
                        }
                        optionQrys.add(optionQry);
                    });
                    oxoRowsQry.setOptions(optionQrys);
                }
                rowsQryList.add(oxoRowsQry);
            });
        });

        qry.setOxoRowsResps(rowsQryList);
        return qry;
    }

    private List<String> checkDeleteOptionPackage(List<OxoOptionPackageAggr> optionPackageAggrList, Map<Long, OxoFeatureOptionAggr> featureOptionMapById) {
        List<String> messageList = Lists.newArrayList();
        Set<String> messageCodeSet = Sets.newHashSet();
        Map<Long, List<OxoOptionPackageAggr>> optionPackageAggrMap = LambdaUtil.groupBy(optionPackageAggrList, OxoOptionPackageAggr::getFeatureOptionId);
        optionPackageAggrMap.forEach((featureOptionId, aggrList) -> {
            // Option的打点全为-，跳过
            if (aggrList.stream().allMatch(OxoOptionPackageAggr::isPackageUnavailable)) {
                return;
            }
            // todo: 最新Formal版本OXO判断
            OxoFeatureOptionAggr featureOptionAggr = featureOptionMapById.get(featureOptionId);
            OxoFeatureOptionAggr parent = featureOptionAggr.getParent();
            if (parent != null && !messageCodeSet.contains(parent.getFeatureCode())) {
                // 勾选Feature的提示
                messageCodeSet.add(parent.getFeatureCode());
                messageList.add("The Options In Feature " + parent.getFeatureCode() + " Has Valid Assignment(Not \"-\")!");
            } else {
                // 勾选Option的提示
                messageList.add("Option " + featureOptionAggr.getFeatureCode() + " Has Valid Assignment(Not \"-\")!");
            }
        });
        return messageList;
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

        return featureRepository.findFeatureLibraryNotFeatureCodes(featureCodes);
    }




    @Override
    public  List<String> checkRules(String modelCode){
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

        return messages;
    }

}
