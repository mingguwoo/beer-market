package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.oxo.OxoPackageInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.oxo.OxoRowInfoAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.feature.oxo.OxoVersionSnapshotAggr;

import java.util.List;

/**
 * @author wangchao.wang
 */
public interface OxoRepository {





    void insertOxoRows(List<OxoRowInfoAggr> rowInfoAggrs);





    void insertOxoOptionPackageInfo(List<OxoPackageInfoAggr> oxoPackageInfoAggrs);




    List<OxoVersionSnapshotAggr>  queryOxoVersionSnapshotLists(String modelCode);








}
