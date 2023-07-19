package com.nio.ngfs.plm.bom.configuration.infrastructure.repository.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nio.ngfs.plm.bom.configuration.infrastructure.repository.entity.BomsOxoRowInfoEntity;

import java.util.List;

/**
 * @author wangchao.wang
 */
public interface BomsOxoRowInfoDao extends IService<BomsOxoRowInfoEntity> {


    void insertOxoRows(List<BomsOxoRowInfoEntity> bomsOxoRowInfoEntityList);


}
