package com.nio.ngfs.plm.bom.configuration.domain.model.oxo.repository;


import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoPackageInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoRowInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoVersionSnapshotAggr;

import java.util.List;

/**
 * @author wangchao.wang
 */
public interface OxoRepository {





    void insertOxoRows(List<OxoRowInfoAggr> rowInfoAggrs);





    void insertOxoOptionPackageInfo(List<OxoPackageInfoAggr> oxoPackageInfoAggrs);




    List<OxoVersionSnapshotAggr>  queryOxoVersionSnapshotLists(String modelCode);

    /**
     *  校验BaseVehicle是否已发布
     * @param modelCode
     */
    boolean checkBaseVehicleStatusCommand (String modelCode);









}
