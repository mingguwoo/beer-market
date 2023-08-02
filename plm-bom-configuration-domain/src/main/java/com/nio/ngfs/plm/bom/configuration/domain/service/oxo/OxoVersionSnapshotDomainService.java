package com.nio.ngfs.plm.bom.configuration.domain.service.oxo;

import com.nio.ngfs.plm.bom.configuration.domain.model.oxoversionsnapshot.OxoVersionSnapshotAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface OxoVersionSnapshotDomainService {


    /**
     * 根据车型
     * @param modelCode
     * @param type
     * @return
     */
      String  queryVersionByModelCode(String modelCode,String type);






}
