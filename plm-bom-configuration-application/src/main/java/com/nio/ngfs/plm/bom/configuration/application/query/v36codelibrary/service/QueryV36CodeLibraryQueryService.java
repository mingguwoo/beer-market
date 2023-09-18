package com.nio.ngfs.plm.bom.configuration.application.query.v36codelibrary.service;

import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsFeatureLibraryEntity;

import java.util.List;

/**
 * @author bill.wang
 * @date 2023/9/18
 */
public interface QueryV36CodeLibraryQueryService {

    /**
     * 获取Catalog为Sales的Feature
     * @return
     */
    List<BomsFeatureLibraryEntity> querySalesFeature();
}
