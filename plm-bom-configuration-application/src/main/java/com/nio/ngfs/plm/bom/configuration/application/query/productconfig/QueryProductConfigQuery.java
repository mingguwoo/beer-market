package com.nio.ngfs.plm.bom.configuration.application.query.productconfig;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nio.bom.share.constants.CommonConstants;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.productconfig.assemble.QueryProductConfigAssembler;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.util.ModelYearComparator;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.*;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.*;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.request.QueryProductConfigQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.productconfig.response.QueryProductConfigRespDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 查询Product Config列表
 *
 * @author xiaozhou.tu
 * @date 2023/8/22
 */
@Component
@RequiredArgsConstructor
public class QueryProductConfigQuery extends AbstractQuery<QueryProductConfigQry, QueryProductConfigRespDto> {

    private final BomsProductConfigDao productConfigDao;
    private final BomsProductConfigModelOptionDao productConfigModelOptionDao;
    private final BomsFeatureLibraryDao featureLibraryDao;
    private final BomsProductConfigOptionDao productConfigOptionDao;
    private final BomsProductContextDao productContextDao;

    @Override
    protected void validate(QueryProductConfigQry qry) {
        if (qry.isEdit() || qry.isShowDiff()) {
            // 编辑或Show Diff模式下，groupList和search置为空
            qry.setGroupList(null);
            qry.setSearch(null);
        }
    }

    @Override
    protected QueryProductConfigRespDto executeQuery(QueryProductConfigQry qry) {
        QueryProductConfigRespDto response = new QueryProductConfigRespDto();
        // 查询并过滤PC列表
        List<BomsProductConfigEntity> pcList = queryPcList(qry, response);
        // 查询并过滤Feature/Option行
        queryModelFeatureOptionList(qry, response);
        // 查询单车Option配置
        queryPcOptionConfig(qry, response, pcList);
        // 比较Option行的PC勾选差异
        handlePcOptionConfigDiff(qry, response);
        return response;
    }

    /**
     * 查询单车Option配置
     */
    private void queryPcOptionConfig(QueryProductConfigQry qry, QueryProductConfigRespDto response, List<BomsProductConfigEntity> pcList) {
        if (CollectionUtils.isEmpty(response.getFeatureList()) || CollectionUtils.isEmpty(response.getPcList())) {
            // Feature/Option行或PC列为空，跳过
            return;
        }
        // 查询Option勾选
        List<BomsProductConfigOptionEntity> optionEntityList = productConfigOptionDao.queryByPcIdList(LambdaUtil.map(response.getPcList(),
                QueryProductConfigRespDto.PcDto::getPcPkId), null);
        // 查询Product Context勾选（只针对编辑模式下）
        List<BomsProductContextEntity> productContextEntityList = null;
        if (qry.isEdit()) {
            productContextEntityList = productContextDao.queryByModelAndModelYearList(qry.getModel(), LambdaUtil.map(response.getPcList(),
                    QueryProductConfigRespDto.PcDto::getModelYear, true));
        }
        // Product Config和Product Context按Option分组
        Map<String, List<BomsProductConfigOptionEntity>> optionEntityGroup = LambdaUtil.groupBy(optionEntityList, BomsProductConfigOptionEntity::getOptionCode);
        Map<String, List<BomsProductContextEntity>> productContextEntityGroup = LambdaUtil.groupBy(productContextEntityList, BomsProductContextEntity::getOptionCode);
        response.getFeatureList().forEach(feature -> {
            feature.getOptionList().forEach(option ->
                    buildPcOptionConfig(option, optionEntityGroup.get(option.getOptionCode()), productContextEntityGroup.get(option.getOptionCode()), pcList, qry.isEdit())
            );
            Map<String, List<QueryProductConfigRespDto.PcOptionConfigDto>> configByPcIdMap =
                    feature.getOptionList().stream().map(QueryProductConfigRespDto.OptionDto::getConfigList)
                            .flatMap(Collection::stream).collect(Collectors.groupingBy(QueryProductConfigRespDto.PcOptionConfigDto::getPcId));
            // Feature是否跳过SkipCheck校验，满足: Feature下所有的Option都跳过SkipCheck校验
            if (qry.isEdit()) {
                feature.setConfigList(LambdaUtil.map(pcList, pc ->
                        QueryProductConfigAssembler.assemble(pc, configByPcIdMap.get(pc.getPcId()).stream()
                                .allMatch(QueryProductConfigRespDto.PcOptionConfigDto::isIgnoreSkipCheck))
                ));
            }
        });
    }

    /**
     * 构建单车Option配置
     */
    private void buildPcOptionConfig(QueryProductConfigRespDto.OptionDto optionDto, List<BomsProductConfigOptionEntity> optionEntityList,
                                     List<BomsProductContextEntity> productContextEntityList, List<BomsProductConfigEntity> pcList,
                                     boolean edit) {
        Map<Long, BomsProductConfigOptionEntity> optionEntityMap = LambdaUtil.toKeyMap(optionEntityList, BomsProductConfigOptionEntity::getPcId);
        Map<String, BomsProductContextEntity> productContextEntityMap = LambdaUtil.toKeyMap(productContextEntityList, BomsProductContextEntity::getModelYear);
        optionDto.setConfigList(LambdaUtil.map(pcList, pc ->
                QueryProductConfigAssembler.assemble(pc, optionEntityMap.get(pc.getId()), productContextEntityMap.get(pc.getModelYear()), edit))
        );
    }

    /**
     * 查询并过滤PC列表
     */
    private List<BomsProductConfigEntity> queryPcList(QueryProductConfigQry qry, QueryProductConfigRespDto response) {
        // 查询PC列表
        List<BomsProductConfigEntity> productConfigEntityList = productConfigDao.queryByModelAndModelYearList(qry.getModel(), qry.getModelYearList());
        if (CollectionUtils.isNotEmpty(qry.getPcIdList())) {
            // 过滤PC
            Set<String> filterPcIdSet = Sets.newHashSet(qry.getPcIdList());
            productConfigEntityList = LambdaUtil.map(productConfigEntityList, i -> filterPcIdSet.contains(i.getPcId()), Function.identity());
        }
        // 排序，先按Model Year排序，再按创建时间正排
        productConfigEntityList = productConfigEntityList.stream().sorted(Comparator.comparing(BomsProductConfigEntity::getModelYear, ModelYearComparator.INSTANCE)
                .thenComparing(BomsProductConfigEntity::getCreateTime)).toList();
        // 组装结果
        List<QueryProductConfigRespDto.PcDto> pcList = LambdaUtil.map(productConfigEntityList, QueryProductConfigAssembler::assemble);
        response.setPcList(pcList);
        return productConfigEntityList;
    }

    /**
     * 查询并过滤Feature/Option行
     */
    private void queryModelFeatureOptionList(QueryProductConfigQry qry, QueryProductConfigRespDto response) {
        // 查询Option行
        List<BomsProductConfigModelOptionEntity> optionEntityList = productConfigModelOptionDao.queryByModel(qry.getModel());
        // 查询Feature Library
        List<BomsFeatureLibraryEntity> featureLibraryEntityList = featureLibraryDao.queryAll();
        // Option行按Feature分组
        Map<String, List<BomsProductConfigModelOptionEntity>> featureGroup = LambdaUtil.groupBy(optionEntityList, BomsProductConfigModelOptionEntity::getFeatureCode);
        Map<String, BomsFeatureLibraryEntity> featureLibraryEntityMap = LambdaUtil.toKeyMap(featureLibraryEntityList, BomsFeatureLibraryEntity::getFeatureCode);
        Set<String> queryGroupSet = Sets.newHashSet(Optional.ofNullable(qry.getGroupList()).orElse(Lists.newArrayList()));
        List<QueryProductConfigRespDto.FeatureDto> featureList = featureGroup.keySet().stream()
                // 构建FeatureDto
                .map(key -> buildFeatureDto(key, featureLibraryEntityMap))
                // 过滤Group
                .filter(feature -> queryGroupSet.isEmpty() || queryGroupSet.contains(feature.getGroup()))
                // 过滤并构建子节点列表
                .peek(feature -> {
                    boolean featureMatchSearch = feature.isMatchSearch(qry.getSearch());
                    feature.setOptionList(featureGroup.getOrDefault(feature.getFeatureCode(), Lists.newArrayList()).stream()
                            // 构建OptionDto
                            .map(option -> buildOptionDto(option, featureLibraryEntityMap, feature.getGroup()))
                            // 模糊匹配，匹配Feature或Option
                            .filter(option -> featureMatchSearch || option.isMatchSearch(qry.getSearch()))
                            // 按Option Code排序
                            .sorted(Comparator.comparing(QueryProductConfigRespDto.OptionDto::getOptionCode))
                            .toList());
                })
                // 过滤Feature，子节点列表不为空
                .filter(feature -> CollectionUtils.isNotEmpty(feature.getOptionList()))
                // 按Group、Feature Code排序
                .sorted(Comparator.comparing(QueryProductConfigRespDto.FeatureDto::getGroup)
                        .thenComparing(QueryProductConfigRespDto.FeatureDto::getFeatureCode))
                .toList();
        response.setFeatureList(featureList);
    }

    private QueryProductConfigRespDto.FeatureDto buildFeatureDto(String featureCode, Map<String, BomsFeatureLibraryEntity> featureLibraryEntityMap) {
        BomsFeatureLibraryEntity featureLibraryEntity = featureLibraryEntityMap.get(featureCode);
        if (featureLibraryEntity == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_FEATURE_NOT_EXISTS);
        }
        return QueryProductConfigAssembler.assemble(featureLibraryEntity);
    }

    private QueryProductConfigRespDto.OptionDto buildOptionDto(BomsProductConfigModelOptionEntity optionEntity, Map<String, BomsFeatureLibraryEntity> featureLibraryEntityMap,
                                                               String group) {
        BomsFeatureLibraryEntity featureLibraryEntity = featureLibraryEntityMap.get(optionEntity.getOptionCode());
        if (featureLibraryEntity == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_OPTION_NOT_EXISTS);
        }
        return QueryProductConfigAssembler.assemble(featureLibraryEntity, group);
    }

    /**
     * 比较Option行的PC勾选差异
     */
    private void handlePcOptionConfigDiff(QueryProductConfigQry qry, QueryProductConfigRespDto response) {
        if (!qry.isShowDiff()) {
            return;
        }
        if (CollectionUtils.isEmpty(response.getFeatureList()) || CollectionUtils.isEmpty(response.getPcList())) {
            return;
        }
        // PC列表小于等于1，不处理
        if (response.getPcList().size() <= CommonConstants.INT_ONE) {
            return;
        }
        response.setFeatureList(response.getFeatureList().stream()
                .peek(feature ->
                        feature.setOptionList(
                                feature.getOptionList().stream().filter(this::isAllPcOptionConfigNotTheSame).toList()
                        )
                )
                .filter(feature -> CollectionUtils.isNotEmpty(feature.getOptionList()))
                .toList());
    }

    /**
     * Option行的PC勾选是否有差异
     */
    private boolean isAllPcOptionConfigNotTheSame(QueryProductConfigRespDto.OptionDto optionDto) {
        boolean select = optionDto.getConfigList().get(0).isSelect();
        return optionDto.getConfigList().stream().anyMatch(i -> select != i.isSelect());
    }

}
