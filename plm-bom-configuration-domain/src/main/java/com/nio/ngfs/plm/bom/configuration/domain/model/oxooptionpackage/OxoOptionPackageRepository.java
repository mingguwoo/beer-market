package com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage;

import com.nio.bom.share.domain.repository.Repository;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxo.OxoPackageInfoAggr;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface OxoOptionPackageRepository extends Repository<OxoOptionPackageAggr, Long> {

    /**
     * oxo打点
     *
     * @param oxoPackages 列表
     */
    void insertOxoOptionPackages(List<OxoPackageInfoAggr> oxoPackages);

    /**
     * 根据basevehicle获取它所有的点
     *
     * @param baseVehicleId
     * @return OxoPackageInfoAggr 列表
     */
    List<OxoOptionPackageAggr> queryByBaseVehicleId(Long baseVehicleId);

    /**
     * 批量新增oxo点
     */
    void inserOxoOptionPackagesByModelCodeAndOxoOptionPackages(List<OxoOptionPackageAggr> oxoOptionPackageAggrs);
}
