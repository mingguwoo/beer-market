package com.sh.beer.market.infrastructure.repository.dao;

import com.baomidou.mybatisplus.service.IService;
import com.sh.beer.market.infrastructure.repository.entity.BomsBaseVehicleEntity;

/**
 * @author
 * @date 2023/7/18
 */
public interface BomsBaseVehicleDao extends IService<BomsBaseVehicleEntity> {

    /**
     * 根据Model,Model Year, Region, Drive Hand,Sales Version查找
     *
     * @param modelCode
     * @param modelYear
     * @param regionOptionCode
     * @param driveHand
     * @param salesVersion
     * @return BaseVehicleAggr列表
     *//*
    List<BomsBaseVehicleEntity> queryByModelCodeModelYearRegionOptionCodeDriveHandSalesVersion(String modelCode, String modelYear, String regionOptionCode, String driveHand, String salesVersion);

    *//**
     * 获取最新BaseVehicle记录的id
     *
     * @return id
     *//*
    Long getLastestBaseVehicle();

    *//**
     * 根据Base Vehicle ID 获取 Base Vehicle
     *
     * @return BomsBasicVehicleEntity
     *//*
    BomsBaseVehicleEntity queryBaseVehicleByBaseVehicleId(String baseVehicleId);

    *//**
     * 获取全量Base Vehicle
     *
     * @return List<BomsBasicVehicleEntity>
     *//*
    List<BomsBaseVehicleEntity> queryAll();

    List<BomsBaseVehicleEntity> queryByModel(String modelCode,Boolean isMaturity);

    *//**
     * 获取同一ModeCode下Status为active的Model
     *
     * @return List<BomsBasicVehicleEntity>
     *//*
    List<BomsBaseVehicleEntity> queryCopyFromModel(String modelCode);

    *//**
     * 根据id列表查询
     * @param idList id列表
     * @return BomsBaseVehicleEntity列表
     *//*
    List<BomsBaseVehicleEntity> queryByIdList(List<Long> idList);

    *//**
     * 根据id列表 active查询
     * @param headIds
     * @return
     *//*
    List<BomsBaseVehicleEntity>  queryByModelCodeAndIdsAndActive(List<Long> headIds,String modelCode);*/
}
