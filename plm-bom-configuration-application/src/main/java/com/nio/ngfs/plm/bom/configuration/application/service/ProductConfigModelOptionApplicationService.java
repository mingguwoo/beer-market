package com.nio.ngfs.plm.bom.configuration.application.service;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;

/**
 * @author xiaozhou.tu
 * @date 2023/8/21
 */
public interface ProductConfigModelOptionApplicationService {

    /**
     * OXO发布同步Feature/Option行到Product Config
     *
     * @param oxoVersionSnapshotAggr OxoVersionSnapshotAggr
     */
    void syncFeatureOptionFromOxoRelease(OxoVersionSnapshotAggr oxoVersionSnapshotAggr);

}
