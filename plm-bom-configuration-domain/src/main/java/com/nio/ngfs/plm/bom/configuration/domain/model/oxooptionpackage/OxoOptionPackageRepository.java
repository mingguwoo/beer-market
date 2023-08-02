package com.nio.ngfs.plm.bom.configuration.domain.model.oxooptionpackage;

import com.nio.bom.share.domain.repository.Repository;

import java.util.List;

/**
 * @author xiaozhou.tu
 * @date 2023/7/27
 */
public interface OxoOptionPackageRepository extends Repository<OxoOptionPackageAggr, Long> {

    /**
     * 批量保存
     *
     * @param aggrList 聚合根列表
     */
    void batchSave(List<OxoOptionPackageAggr> aggrList);

    /**
     * oxo打点
     *
     * @param oxoPackages 列表
     */
    void insertOxoOptionPackages(List<OxoOptionPackageAggr> oxoPackages);

    /**
     * 根据basevehicle获取它所有的点
     *
     * @param baseVehicleId
     * @return OxoPackageInfoAggr 列表
     */
    List<OxoOptionPackageAggr> queryByBaseVehicleId(Long baseVehicleId);

    /**
     * 根据featureOptionId列表查询
     *
     * @param featureOptionIdList featureOptionId列表
     * @return OxoOptionPackageAggr列表
     */
    List<OxoOptionPackageAggr> queryByFeatureOptionIdList(List<Long> featureOptionIdList);

    /**
     * 批量新增oxo点
     */
    void inserOxoOptionPackagesByOxoOptionPackages(List<OxoOptionPackageAggr> oxoOptionPackageAggrs);


    /**
     * 根据 行id 查询打点信息
     * @param rowIds
     * @return
     */
    List<OxoOptionPackageAggr> queryByBaseVehicleIds(List<Long> rowIds);

    void saveOrUpdatebatch(List<OxoOptionPackageAggr> oxoOptionPackageAggrs);
}
