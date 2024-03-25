package com.sh.beer.market.domain.service.basevehicle;

/**
 * @author
 * @date 2023/7/19
 */
public interface BaseVehicleDomainService {

    /**
     * 校验Base Vehicle 是否唯一
     *
     * @param baseVehicleAggr 聚合根
     *//*
    void checkBaseVehicleUnique(BaseVehicleAggr baseVehicleAggr);

    *//**
     * 根据BaseVehicleId获取BaseVehicle
     *
     * @param baseVehicleId id
     *//*
    BaseVehicleAggr getBaseVehicleByBaseVehicleId(String baseVehicleId);

    *//**
     * 查询oxo 头节点信息
     * @param modelCode
     * @return
     *//*
    List<OxoHeadQry> queryByModel(String modelCode,Boolean isMaturity);

    *//**
     * 校验BaseVehicle是否存在
     *//*
    void checkBaseVehicleExist(BaseVehicleAggr baseVehicleAggr);
*/
}
