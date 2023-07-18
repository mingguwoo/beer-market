package com.nio.ngfs.plm.bom.configuration.domain.model.feature;

import com.nio.ngfs.plm.bom.configuration.domain.model.feature.oxo.OxoRowInfoAggr;

import java.util.List;

/**
 * @author wangchao.wang
 */
public interface OxoRepository {





    void insertOxoRows(List<OxoRowInfoAggr> rowInfoAggrs);




    void insertOxoOptionPackageInfo(List<OxoRowInfoAggr> rowInfoAggrs);




}
