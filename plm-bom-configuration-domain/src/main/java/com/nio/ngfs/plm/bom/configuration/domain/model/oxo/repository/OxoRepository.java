package com.nio.ngfs.plm.bom.configuration.domain.model.oxo.repository;


import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoPackageInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoRowInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoVersionSnapshotAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.domainobject.OxoInfoDo;

import java.util.List;

/**
 * @author wangchao.wang
 */
public interface OxoRepository {





    void insertOxoRows(List<OxoRowInfoAggr> rowInfoAggrs);





    void insertOxoOptionPackageInfo(List<OxoPackageInfoAggr> oxoPackageInfoAggrs);




    List<OxoVersionSnapshotAggr>  queryOxoVersionSnapshotLists(String modelCode);


    /**
     * 根据modelCode查询oxo
     * @param modelCode
     * @return
     */
    List<OxoInfoDo>  queryFeatureListsByModel(String modelCode);








}
