package com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary.service.impl;

import com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary.service.QueryV36CodeLibraryQueryService;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.enums.FeatureCatalogEnum;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsFeatureLibraryDao;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/18
 */
@Component
@RequiredArgsConstructor
public class QueryV36CodeLibraryQueryServiceImpl implements QueryV36CodeLibraryQueryService {


    private final BomsFeatureLibraryDao bomsFeatureLibraryDao;

    @Override
    public List<BomsFeatureLibraryEntity> querySalesFeature() {
        return bomsFeatureLibraryDao.queryByType(FeatureCatalogEnum.SALES.getCatalog());
    }
}
