package com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface OxoVersionSnapshotRepository extends Repository<OxoVersionSnapshotAggr, Long> {




      List<OxoVersionSnapshotAggr> queryOxoVersionSnapshotByModelCode(String modelCode);



}
