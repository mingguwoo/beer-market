package com.nio.ngfs.plm.bom.configuration.application.query.feature;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureCatalogEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.QueryFeatureCodeByCatalogQry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author bill.wang
 * @date 2023/9/19
 */
@Component
@RequiredArgsConstructor
public class QueryFeatureCodeByCatalogQuery extends AbstractQuery<QueryFeatureCodeByCatalogQry,List<String>> {

    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    public List<String> executeQuery(QueryFeatureCodeByCatalogQry qry) {
        checkType(qry);
        return bomsFeatureLibraryDao.queryByCatalog(qry.getCatalog()).stream().map(entity->entity.getFeatureCode()).distinct().sorted().toList();
    }

    private void checkType(QueryFeatureCodeByCatalogQry qry){
        if (Objects.nonNull(qry.getCatalog()) && Objects.isNull(FeatureCatalogEnum.getByCatalog(qry.getCatalog()))){
            throw new BusinessException(ConfigErrorCode.FEATURE_CATALOG_INVALID);
        }
    }
}
