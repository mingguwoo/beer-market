package com.nio.ngfs.plm.bom.configuration.domain.service.oxo;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.sdk.dto.baseVehicle.request.DeleteBaseVehicleCmd;

import java.util.List;

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
      String  queryVersionByModelCode(String modelCode,String type);


    /**
     * 根据 车型和版本 查询快照版本
     * @param modelCode
     * @param version
     * @return
     */
     OxoVersionSnapshotAggr queryOxoInfoByModelAndVersion(String modelCode,String version);


      void checkBaseVehicleReleased(String modelCode);



}
