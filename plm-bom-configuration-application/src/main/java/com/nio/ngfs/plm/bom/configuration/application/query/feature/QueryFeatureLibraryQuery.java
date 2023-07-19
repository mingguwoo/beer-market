package com.nio.ngfs.plm.bom.configuration.application.query.feature;

import com.nio.bom.share.exception.BusinessException;
import com.nio.bom.share.utils.LambdaUtil;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.assemble.FeatureLibraryAssembler;
import com.nio.ngfs.plm.bom.configuration.application.query.feature.common.FeatureLibraryQueryUtil;
import com.nio.ngfs.plm.bom.configuration.common.enums.CatalogEnum;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.common.enums.StatusEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.QueryFeatureLibraryQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.QueryFeatureLibraryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
        if (qry.getCatalog() != null && CatalogEnum.getByCatalog(qry.getCatalog()) == null) {
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
        // 过滤Group、Catalog、Status、搜索词
        List<BomsFeatureLibraryEntity> filteredTree = filter(featureLibraryTree, qry);
        // 排序
        List<BomsFeatureLibraryEntity> sortedTree = FeatureLibraryQueryUtil.sortFeatureLibraryTree(filteredTree);
        // 组装为DTO
        List<QueryFeatureLibraryDto> dtoList = LambdaUtil.map(sortedTree, FeatureLibraryAssembler::assemble);
        // 处理RelatedModel
        handleRelatedModel(dtoList);
        return dtoList;
    }

    private List<BomsFeatureLibraryEntity> filter(List<BomsFeatureLibraryEntity> featureLibraryTree, QueryFeatureLibraryQry qry) {
        return featureLibraryTree;
    }

    private void handleRelatedModel(List<QueryFeatureLibraryDto> dtoList) {
    }

}
