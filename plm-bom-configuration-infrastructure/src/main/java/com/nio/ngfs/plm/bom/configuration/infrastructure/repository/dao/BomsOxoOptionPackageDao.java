package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage.OxoOptionPackageAggr;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoOptionPackageEntity;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface BomsOxoOptionPackageDao extends IService<BomsOxoOptionPackageEntity> {



    List<BomsOxoOptionPackageEntity> queryOxoOptionPackageByRowIds(List<Long> rowIds);



    void insertOxoOptionPackages(List<OxoOptionPackageAggr> oxoPackages);



    /**
     * 根据basevehicle获取它所有的点
     *
     * @param baseVehicleId
     * @return OxoPackageInfoAggr 列表
     */
    List<BomsOxoOptionPackageEntity> queryOxoListByBaseVehicle(Long baseVehicleId);
}
