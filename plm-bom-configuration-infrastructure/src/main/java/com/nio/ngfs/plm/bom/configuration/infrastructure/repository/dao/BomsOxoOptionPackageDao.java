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



    void insertOxoOptionPackages(List<BomsOxoOptionPackageEntity> oxoPackages);

    /**
     * 根据featureOptionId列表查询
     *
     * @param featureOptionIdList featureOptionId列表
     * @return BomsOxoOptionPackageEntity列表
     */
    List<BomsOxoOptionPackageEntity> queryByFeatureOptionIdList(List<Long> featureOptionIdList,List<Long> headIds);

    /**
     * 根据basevehicle获取它所有的点
     *
     * @param baseVehicleId
     * @return OxoPackageInfoAggr 列表
     */
    List<BomsOxoOptionPackageEntity> queryOxoListByBaseVehicle(Long baseVehicleId);


    /**
     * 根据basevehicle获取它所有的点
     *
     * @param baseVehicleId
     * @return OxoPackageInfoAggr 列表
     */
    void removeByBaseVehicleIds(Long baseVehicleId);
}
