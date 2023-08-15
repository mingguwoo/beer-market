package com.nio.ngfs.plm.bom.configuration.domain.service.oxo;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.oxo.response.OxoListQry;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface OxoVersionSnapshotDomainService {


    /**
     * 根据车型 和 type获取 OXO版本
     * @param modelCode
     * @param type
     * @return
     */
    OxoVersionSnapshotAggr  queryVersionByModelCode(String modelCode,String type);


    /**
     * 根据 车型和版本 查询快照版本
     * @param modelCode
     * @param version
     * @return
     */
     OxoVersionSnapshotAggr queryOxoInfoByModelAndVersion(String modelCode,String version);

    /**
     * 校验BaseVehicle是否在oxo中是否已有发布版本
     */
      void checkBaseVehicleReleased(String modelCode);

    /**
     * 解析OxoSnapShot字符串
     */
      OxoListQry resolveSnapShot(String oxoSnapShot);


}
