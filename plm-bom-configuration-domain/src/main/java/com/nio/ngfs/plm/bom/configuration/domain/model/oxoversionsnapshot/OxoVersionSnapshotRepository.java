package com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot;

import com.nio.bom.share.domain.repository.Repository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.enums.OxoSnapshotEnum;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface OxoVersionSnapshotRepository extends Repository<OxoVersionSnapshotAggr, Long> {

      /**
       * 根据车型查询最新Release的OXO版本
       *
       * @param modelCode 车型
       * @param type 类型
       * @return OxoVersionSnapshotAggr
       */
      OxoVersionSnapshotAggr queryLastReleaseSnapshotByModel(String modelCode, OxoSnapshotEnum type);

      /**
       * 查询oxo版本
       * @param modelCode
       * @return
       */
      List<OxoVersionSnapshotAggr> queryOxoVersionSnapshotByModelCode(String modelCode, String version, String type);


      /**
       * 查询BaseVehicle是否有已发布的oxo版本
       */
      List<OxoVersionSnapshotAggr> queryBomsOxoVersionSnapshotsByModel(String modelCode);

}
