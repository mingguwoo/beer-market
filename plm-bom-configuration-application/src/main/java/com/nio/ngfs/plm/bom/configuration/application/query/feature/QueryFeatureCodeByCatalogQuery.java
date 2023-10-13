package com.nio.ngfs.plm.bom.configuration.application.query.feature;

import com.nio.bom.share.exception.BusinessException;
import com.nio.ngfs.plm.bom.configuration.application.query.AbstractQuery;
import com.nio.ngfs.plm.bom.configuration.common.enums.ConfigErrorCode;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureCatalogEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.request.QueryFeatureCodeByCatalogQry;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.feature.response.QueryFeatureLibraryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author bill.wang
 * @date 2023/9/19
 */
@Component
@RequiredArgsConstructor
public class QueryFeatureCodeByCatalogQuery extends AbstractQuery<QueryFeatureCodeByCatalogQry,List<QueryFeatureLibraryDto>> {

    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    public List<QueryFeatureLibraryDto> executeQuery(QueryFeatureCodeByCatalogQry qry) {
        checkType(qry);
        List<BomsFeatureLibraryEntity> entityList =  bomsFeatureLibraryDao.queryFeatureByCatalog(qry.getCatalog());
        List<QueryFeatureLibraryDto> dtoList = new ArrayList<>();
        entityList.forEach(entity->{
            QueryFeatureLibraryDto dto = new QueryFeatureLibraryDto();
            dto.setFeatureCode(entity.getFeatureCode());
            dto.setChineseName(entity.getChineseName());
            dto.setDisplayName(entity.getDisplayName());
            dtoList.add(dto);
        });
        return dtoList;
    }

    private void checkType(QueryFeatureCodeByCatalogQry qry){
        if (Objects.nonNull(qry.getCatalog()) && Objects.isNull(FeatureCatalogEnum.getByCatalog(qry.getCatalog()))){
            throw new BusinessException(ConfigErrorCode.FEATURE_CATALOG_INVALID);
        }
    }
}
