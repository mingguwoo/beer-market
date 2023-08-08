package com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface OxoVersionSnapshotRepository extends Repository<OxoVersionSnapshotAggr, Long> {


      /**
       * 查询oxo版本
       * @param modelCode
       * @return
       */
      List<OxoVersionSnapshotAggr> queryOxoVersionSnapshotByModelCode(String modelCode);


      /**
       * 查询BaseVehicle是否有已发布的oxo版本
       */
      List<OxoVersionSnapshotAggr> queryBomsOxoVersionSnapshotsByModel(String modelCode);





}
