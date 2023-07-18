package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.impl;

import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleAggr;
import com.nio.ngfs.plm.bom.configuration.domain.model.baseVehicle.BaseVehicleRepository;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao.BomsBasicVehicleDao;

/**
 * @author bill.wang
 * @date 2023/7/18
 */
public class BaseVehicleRepositoryImpl implements BaseVehicleRepository {

    private final BomsBasicVehicleDao bomsBasicVehicleDao;

    public BaseVehicleRepositoryImpl(BomsBasicVehicleDao bomsBasicVehicleDao) {
        this.bomsBasicVehicleDao = bomsBasicVehicleDao;
    }

    @Override
    public void save(BaseVehicleAggr baseVehicleAggr) {
//        DaoSupport.saveOrUpdate(B);
    }

    @Override
    public BaseVehicleAggr find(String s) {
        return null;
    }
}
