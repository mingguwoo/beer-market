package com.nio.ngfs.plm.bom.configuration.application.query.feature;

import com.google.common.collect.Sets;
import com.nio.bom.share.enums.StatusEnum;
import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.assemble.FeatureLibraryAssembler;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.common.FeatureLibraryQueryUtil;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureCatalogEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.QueryFeatureLibraryQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.QueryFeatureLibraryDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 查询Feature Library列表
 *
 * @author xiaozhou.tu
 * @date 2023/6/28
 */
@Component
@RequiredArgsConstructor
public class QueryFeatureLibraryQuery extends AbstractQuery<QueryFeatureLibraryQry, List<QueryFeatureLibraryDto>> {

    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    protected void validate(QueryFeatureLibraryQry qry) {
        // 校验参数
        if (qry.getCatalog() != null && FeatureCatalogEnum.getByCatalog(qry.getCatalog()) == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_CATALOG_INVALID);
        }
        if (qry.getStatus() != null && StatusEnum.getByStatus(qry.getStatus()) == null) {
            throw new BusinessException(ConfigErrorCode.FEATURE_STATUS_INVALID);
        }
    }

    @Override
    public List<QueryFeatureLibraryDto> executeQuery(QueryFeatureLibraryQry qry) {
        // 全量查询Feature Library
        List<BomsFeatureLibraryEntity> entityList = bomsFeatureLibraryDao.queryAll();
        // 组装为树形结构
        List<BomsFeatureLibraryEntity> featureLibraryTree = FeatureLibraryQueryUtil.buildFeatureLibraryTree(entityList);
        // 组装为DTO
        List<QueryFeatureLibraryDto> featureLibraryDtoTree = LambdaUtil.map(featureLibraryTree, FeatureLibraryAssembler::assemble);
        // 过滤Group、Catalog、Status、搜索词
        List<QueryFeatureLibraryDto> filteredTree = filter(featureLibraryDtoTree, qry);
        // 排序
        List<QueryFeatureLibraryDto> sortedTree = sortFeatureLibraryTree(filteredTree);
        // 处理RelatedModel
        handleRelatedModel(sortedTree);
        return sortedTree;
    }

    private List<QueryFeatureLibraryDto> filter(List<QueryFeatureLibraryDto> featureLibraryDtoTree, QueryFeatureLibraryQry qry) {
        // 过滤Group
        if (CollectionUtils.isNotEmpty(qry.getGroupCodeList())) {
            Set<String> groupCodeSet = Sets.newHashSet(qry.getGroupCodeList());
            featureLibraryDtoTree = featureLibraryDtoTree.stream().filter(group -> groupCodeSet.contains(group.getFeatureCode())).toList();
        }
        // 过滤Catalog
        if (StringUtils.isNotBlank(qry.getCatalog())) {
            featureLibraryDtoTree.forEach(group -> {
                group.setChildren(group.getChildren().stream().filter(feature -> Objects.equals(qry.getCatalog(), feature.getCatalog())).toList());
                group.checkChildrenEmpty();
            });
        }
        // 过滤Status
        if (StringUtils.isNotBlank(qry.getStatus())) {
            featureLibraryDtoTree.forEach(group -> {
                group.getChildren().forEach(feature -> {
                    feature.setChildren(feature.getChildren().stream().filter(option -> Objects.equals(qry.getStatus(), option.getStatus())).toList());
                    feature.checkChildrenEmpty();
                });
                group.checkChildrenEmpty();
            });
        }
        // 模糊搜索，Feature Code、Display Name、Chinese Name
        if (StringUtils.isNotBlank(qry.getSearch())) {
            featureLibraryDtoTree.forEach(group -> group.selectSearchMatch(qry.getSearch().trim(), false));
        }
        // 结果筛选
        if (StringUtils.isNotBlank(qry.getCatalog()) || StringUtils.isNotBlank(qry.getStatus()) || StringUtils.isNotBlank(qry.getSearch())) {
            featureLibraryDtoTree = featureLibraryDtoTree.stream().filter(QueryFeatureLibraryDto::matchResult).toList();
            featureLibraryDtoTree.forEach(group -> {
                group.setChildren(group.getChildren().stream().filter(QueryFeatureLibraryDto::matchResult).toList());
                group.getChildren().forEach(feature ->
                        feature.setChildren(feature.getChildren().stream().filter(QueryFeatureLibraryDto::matchResult).toList())
                );
            });
        }
        return featureLibraryDtoTree;
    }

    private static List<QueryFeatureLibraryDto> sortFeatureLibraryTree(List<QueryFeatureLibraryDto> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        entityList.forEach(entity -> entity.setChildren(sortFeatureLibraryTree(entity.getChildren())));
        return entityList.stream().sorted(Comparator.comparing(QueryFeatureLibraryDto::getFeatureCode)).toList();
    }

    private void handleRelatedModel(List<QueryFeatureLibraryDto> dtoList) {
        // todo
    }

}
